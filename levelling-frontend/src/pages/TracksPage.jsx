import React, { useState, useEffect, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../services/api';
import TrackSidebarItem from '../components/TrackSidebarItem';
import ArcCard from '../components/ArcCard';
import TaskModal from '../components/TaskModal';
import ClaimArcModal from '../components/ClaimArcModal';
import styles from './TracksPage.module.css';

const TracksPage = () => {
    const { sagaId } = useParams();
    const [tracks, setTracks] = useState([]);
    const [selectedTrack, setSelectedTrack] = useState(null);
    const [ongoingArcs, setOngoingArcs] = useState([]);
    const [completedArcs, setCompletedArcs] = useState([]);
    const [isLoadingArcs, setIsLoadingArcs] = useState(false);
    const [selectedArcForTasks, setSelectedArcForTasks] = useState(null);
    const [completedTaskIds, setCompletedTaskIds] = useState(new Set());
    const [claimableArcIds, setClaimableArcIds] = useState(new Set());
    const [isClaimModalOpen, setIsClaimModalOpen] = useState(false);
    const [arcToClaim, setArcToClaim] = useState(null);

    const fetchTrackData = useCallback(async (track) => {
        if (!track) return;
        setIsLoadingArcs(true);
        try {
            const [arcsResponse, progressResponse] = await Promise.all([
                api.get(`/content/arcs?trackId=${track.id}`),
                api.get('/users/me/progress')
            ]);

            const allArcs = arcsResponse.data;
            const progress = progressResponse.data;
            const newCompletedTaskIds = new Set(progress.completedTaskIds);

            // --- THIS IS THE CRITICAL FIX ---
            // We are now using the REAL completedArcIds from the user's progress
            const realCompletedArcIds = new Set(progress.completedArcIds);

            setCompletedTaskIds(newCompletedTaskIds);

            const currentOngoingArcs = allArcs.filter(arc => !realCompletedArcIds.has(arc.id));
            const currentCompletedArcs = allArcs.filter(arc => realCompletedArcIds.has(arc.id));

            setOngoingArcs(currentOngoingArcs);
            setCompletedArcs(currentCompletedArcs);

            const newClaimableArcIds = new Set();
            await Promise.all(currentOngoingArcs.map(async (arc) => {
                const tasksRes = await api.get(`/content/tasks?arcId=${arc.id}`);
                const primaryTasks = tasksRes.data.filter(t => t.type === 'PRIMARY');
                const allPrimaryDone = primaryTasks.length > 0 && primaryTasks.every(t => newCompletedTaskIds.has(t.id));
                if (allPrimaryDone) {
                    newClaimableArcIds.add(arc.id);
                }
            }));
            setClaimableArcIds(newClaimableArcIds);

        } catch (error) {
            console.error(`Failed to fetch data for track`, error);
        } finally {
            setIsLoadingArcs(false);
        }
    }, []);

    useEffect(() => {
        api.get(`/content/tracks?sagaId=${sagaId}`)
            .then(response => {
                const fetchedTracks = response.data;
                setTracks(fetchedTracks);
                if (fetchedTracks.length > 0) {
                    setSelectedTrack(fetchedTracks[0]);
                }
            })
            .catch(error => console.error(`Failed to fetch tracks`, error));
    }, [sagaId]);

    useEffect(() => {
        fetchTrackData(selectedTrack);
    }, [selectedTrack, fetchTrackData]);

    const handleTaskCompletion = () => {
        fetchTrackData(selectedTrack);
    };

    const handleClaimClick = (arc) => {
        setArcToClaim(arc);
        setIsClaimModalOpen(true);
    };

    const handleConfirmClaim = async (ratings) => {
        if (!arcToClaim) return;
        try {
            await api.post(`/progress/arcs/${arcToClaim.id}/complete`, ratings);
            fetchTrackData(selectedTrack); // This will now fetch the updated progress
        } catch (error) {
            console.error("Failed to claim arc:", error);
        } finally {
            setIsClaimModalOpen(false);
            setArcToClaim(null);
        }
    };

    // ... (The rest of the JSX return statement is unchanged) ...
    return (
        <>
            {selectedArcForTasks && (
                <TaskModal
                    arc={selectedArcForTasks}
                    onClose={() => setSelectedArcForTasks(null)}
                    onTaskComplete={handleTaskCompletion}
                    initiallyCompletedTaskIds={completedTaskIds}
                />
            )}
            {isClaimModalOpen && arcToClaim && (
                <ClaimArcModal
                    arc={arcToClaim}
                    onCancel={() => setIsClaimModalOpen(false)}
                    onClaim={handleConfirmClaim}
                />
            )}
            <div className={styles.tracksPage}>
                {/* ... (sidebar) ... */}
                <div className={styles.mainContent}>
                    {selectedTrack ? (
                        <>
                            {/* ... (header, description) ... */}
                            <div className={styles.arcsContainer}>
                                <h2>Ongoing Arcs</h2>
                                {isLoadingArcs ? <p>Loading arcs...</p> : (
                                    <div className={styles.ongoingArcsList}>
                                        {ongoingArcs.map(arc => (
                                            <ArcCard
                                                key={arc.id}
                                                arc={arc}
                                                onSelect={setSelectedArcForTasks}
                                                canClaim={claimableArcIds.has(arc.id)}
                                                onClaim={handleClaimClick}
                                            />
                                        ))}
                                    </div>
                                )}
                                <h2 className={styles.completedHeader}>Completed Arcs</h2>
                                <div className={styles.completedArcsList}>
                                    {completedArcs.map(arc => (
                                        <div key={arc.id} className={styles.completedArcCard}>
                                            <span>✅ {arc.name}</span>
                                            <span>+{arc.points} pts</span>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </>
                    ) : (
                        tracks.length > 0 && <p>Select a track to see its details.</p>
                    )}
                </div>
            </div>
        </>
    );
};

export default TracksPage;