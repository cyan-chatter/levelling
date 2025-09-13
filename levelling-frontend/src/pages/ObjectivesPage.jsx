// src/pages/ObjectivesPage.jsx
import React, { useState, useEffect, useCallback } from 'react';
import api from '../services/api';
import styles from './ObjectivesPage.module.css';
import ObjectiveSelectionModal from '../components/ObjectiveSelectionModal';

// --- UPDATED ObjectiveCard ---
// It no longer manages its own state. It receives its completion status as a prop.
const ObjectiveCard = ({ item, type, onComplete, isCompleted }) => {
    const handleComplete = async (e) => {
        e.stopPropagation();
        if (type !== 'task' || isCompleted) return;
        try {
            await api.post(`/progress/tasks/${item.id}/complete`);
            if (onComplete) onComplete(item.id); // Pass the ID back up
        } catch (error) {
            console.error("Failed to complete objective task:", error);
        }
    };

    return (
        <div className={`${styles.objectiveCard} ${isCompleted ? styles.completed : ''}`}>
            <span className={`${styles.itemType} ${styles[type]}`}>{type}</span>
            <p className={styles.itemName}>{item.name}</p>
            <div className={styles.cardActions}>
                <span className={styles.itemPoints}>+{item.points} pts</span>
                {type === 'task' && !isCompleted && (
                    <button className={styles.completeButton} onClick={handleComplete}>
                        Complete
                    </button>
                )}
                {isCompleted && <span className={styles.completedCheck}>✅</span>}
            </div>
        </div>
    );
};


const ObjectivesPage = () => {
    const [dailyObjectives, setDailyObjectives] = useState([]);
    const [weeklyObjectives, setWeeklyObjectives] = useState([]);

    // --- NEW STATE TO HOLD COMPLETED IDs ---
    const [completedTaskIds, setCompletedTaskIds] = useState(new Set());

    const [isLoading, setIsLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalType, setModalType] = useState('daily');

    const fetchData = useCallback(() => {
        setIsLoading(true);
        // Now we fetch three pieces of data: daily objectives, weekly objectives, AND user progress
        Promise.all([
            api.get('/objectives/current/daily'),
            api.get('/objectives/current/weekly'),
            api.get('/users/me/progress')
        ]).then(([dailyRes, weeklyRes, progressRes]) => {
            setDailyObjectives(dailyRes.data);
            setWeeklyObjectives(weeklyRes.data);
            setCompletedTaskIds(new Set(progressRes.data.completedTaskIds));
        }).catch(error => {
            console.error("Failed to fetch objectives data:", error);
        }).finally(() => {
            setIsLoading(false);
        });
    }, []);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const handleTaskCompletion = (completedTaskId) => {
        // Optimistically update the UI to show the task is completed
        setCompletedTaskIds(prev => new Set(prev).add(completedTaskId));
        // In a real app, you would also refresh user currency here.
    };

    const handleSaveObjectives = async (selectedIds) => {
        const endpoint = modalType === 'daily' ? '/objectives/daily' : '/objectives/weekly';
        try {
            await api.post(endpoint, { ids: selectedIds });
            setIsModalOpen(false);
            fetchData(); // Refresh all data after saving
        } catch (error) {
            console.error(`Failed to save objectives:`, error);
        }
    };

    const openModal = (type) => {
        setModalType(type);
        setIsModalOpen(true);
    };

    return (
        <>
            {isModalOpen && (
                <ObjectiveSelectionModal
                    objectiveType={modalType}
                    onClose={() => setIsModalOpen(false)}
                    onSave={handleSaveObjectives}
                />
            )}
            <div className={styles.objectivesPage}>
                <div className={styles.section}>
                    <div className={styles.sectionHeader}>
                        <h2>Daily Objectives</h2>
                        <button className={styles.addButton} onClick={() => openModal('daily')}>Edit Objectives</button>
                    </div>
                    <div className={styles.objectivesList}>
                        {isLoading ? <p>Loading...</p> : (
                            dailyObjectives.length > 0 ? (
                                dailyObjectives.map(task =>
                                    <ObjectiveCard
                                        key={task.id}
                                        item={task}
                                        type="task"
                                        // The card's completion status is determined by the main page's state
                                        isCompleted={completedTaskIds.has(task.id)}
                                        onComplete={handleTaskCompletion}
                                    />)
                            ) : <p>No daily objectives set.</p>
                        )}
                    </div>
                </div>

                <div className={styles.section}>
                    {/* ... weekly objectives section remains the same ... */}
                </div>
            </div>
        </>
    );
};

export default ObjectivesPage;