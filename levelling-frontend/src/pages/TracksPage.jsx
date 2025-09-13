// src/pages/TracksPage.jsx
import React, { useState, useEffect, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../services/api';
import TrackSidebarItem from '../components/TrackSidebarItem';
import ArcCard from '../components/ArcCard';
import TaskModal from '../components/TaskModal';
import styles from './TracksPage.module.css';

const TracksPage = () => {
    const { sagaId } = useParams();
    const [tracks, setTracks] = useState([]);
    const [selectedTrack, setSelectedTrack] = useState(null);
    const [ongoingArcs, setOngoingArcs] = useState([]);
    const [completedArcs, setCompletedArcs] = useState([]);
    const [isLoadingArcs, setIsLoadingArcs] = useState(false);
    const [selectedArcForTasks, setSelectedArcForTasks] = useState(null);

    // --- NEW STATE TO HOLD USER PROGRESS ---
    const [completedTaskIds, setCompletedTaskIds] = useState(new Set());
    const [completedArcIds, setCompletedArcIds] = useState(new Set());

    const fetchTrackData = useCallback((track) => {
        setIsLoadingArcs(true);
        // Fetch all three pieces of data simultaneously
        Promise.all([
            api.get(`/content/arcs?trackId=${track.id}`),
            api.get('/users/me/progress')
        ]).then(([arcsResponse, progressResponse]) => {
            const allArcs = arcsResponse.data;
            const progress = progressResponse.data;

            const newCompletedTaskIds = new Set(progress.completedTaskIds);
            setCompletedTaskIds(newCompletedTaskIds);

            // We need to get completed arc IDs from the full progress object if available
            // For now, let's continue the mock logic until we build the full progress DTO
            const mockCompletedArcIds = new Set(allArcs.length > 0 ? [allArcs[0].id] : []);
            setCompletedArcIds(mockCompletedArcIds);

            // Now filter arcs based on the real (or mocked) progress data
            setOngoingArcs(allArcs.filter(arc => !mockCompletedArcIds.has(arc.id)));
            setCompletedArcs(allArcs.filter(arc => mockCompletedArcIds.has(arc.id)));

        }).catch(error => console.error(`Failed to fetch data for track`, error))
            .finally(() => setIsLoadingArcs(false));
    }, []);

    useEffect(() => {
        api.get(`/content/tracks?sagaId=${sagaId}`)
            .then(response => {
                const fetchedTracks = response.data;
                setTracks(fetchedTracks);
                if (fetchedTracks.length > 0) {
                    setSelectedTrack(fetchedTracks[0]);
                    fetchTrackData(fetchedTracks[0]);
                }
            })
            .catch(error => console.error(`Failed to fetch tracks`, error));
    }, [sagaId, fetchTrackData]);

    const handleTrackSelect = (track) => {
        setSelectedTrack(track);
        fetchTrackData(track);
    };

    const handleTaskCompletion = (completedTaskId) => {
        // Optimistically update the completed tasks set
        setCompletedTaskIds(prev => new Set(prev).add(completedTaskId));
        // Re-fetch all track data to see if an arc's status has changed
        if (selectedTrack) {
            fetchTrackData(selectedTrack);
        }
    };

    return (
        <>
            {selectedArcForTasks && (
                <TaskModal
                    arc={selectedArcForTasks}
                    onClose={() => setSelectedArcForTasks(null)}
                    onTaskComplete={handleTaskCompletion}
                    // Pass the known completed task IDs down to the modal
                    initiallyCompletedTaskIds={completedTaskIds}
                />
            )}

            <div className={styles.tracksPage}>
                <div className={styles.sidebar}>
                    <Link to="/sagas" className={styles.backButton}>&larr; Back to Sagas</Link>
                    {tracks.map(track => (
                        <TrackSidebarItem
                            key={track.id}
                            track={track}
                            onSelect={handleTrackSelect}
                            isActive={selectedTrack?.id === track.id}
                        />
                    ))}
                </div>
                <div className={styles.mainContent}>
                    {selectedTrack && (
                        <>
                            {/* Header and Description remain the same */}
                            <div className={styles.trackHeader}>
                                <img src={selectedTrack.imageUrl || '/track-header-placeholder.jpg'} alt={selectedTrack.name} />
                                <h1>{selectedTrack.name}</h1>
                            </div>
                            <div className={styles.trackDescription}>
                                <p>{selectedTrack.description}</p>
                            </div>

                            <div className={styles.arcsContainer}>
                                <h2>Ongoing Arcs</h2>
                                {isLoadingArcs ? <p>Loading arcs...</p> : (
                                    <div className={styles.ongoingArcsList}>
                                        {ongoingArcs.map(arc => (
                                            <ArcCard key={arc.id} arc={arc} onSelect={setSelectedArcForTasks} />
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
                    )}
                </div>
            </div>
        </>
    );
};

export default TracksPage;