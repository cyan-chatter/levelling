// src/components/TaskModal.jsx
import React, { useState, useEffect, useRef } from 'react';
import api from '../services/api';
import styles from './TaskModal.module.css';
import { gsap } from 'gsap';
import { useGSAP } from '@gsap/react';

const TaskModal = ({ arc, onClose, onTaskComplete, initiallyCompletedTaskIds }) => {
    const [tasks, setTasks] = useState([]);
    // THIS IS THE KEY CHANGE: State is initialized from the prop
    const [completedTaskIds, setCompletedTaskIds] = useState(initiallyCompletedTaskIds || new Set());
    const modalRef = useRef(null);

    useEffect(() => {
        if (arc) {
            api.get(`/content/tasks?arcId=${arc.id}`)
                .then(response => {
                    setTasks(response.data);
                })
                .catch(error => console.error("Failed to fetch tasks:", error));
        }
    }, [arc]);

    useGSAP(() => {
        gsap.fromTo(modalRef.current,
            { opacity: 0, scale: 0.9 },
            { opacity: 1, scale: 1, duration: 0.3, ease: 'power2.out' }
        );
    }, { scope: modalRef });

    const handleCompleteTask = async (taskId) => {
        try {
            await api.post(`/progress/tasks/${taskId}/complete`);
            // Update our local state for instant UI feedback
            setCompletedTaskIds(prev => new Set(prev).add(taskId));
            // Notify the parent (TracksPage) that a task was completed
            onTaskComplete(taskId);
        } catch (error) {
            console.error(`Failed to complete task ${taskId}:`, error);
        }
    };

    if (!arc) return null;

    const ongoingTasks = tasks.filter(task => !completedTaskIds.has(task.id));
    const completedTasks = tasks.filter(task => completedTaskIds.has(task.id));

    return (
        <div className={styles.modalBackdrop} onClick={onClose}>
            <div className={styles.modalContent} onClick={e => e.stopPropagation()} ref={modalRef}>
                <button className={styles.closeButton} onClick={onClose}>&times;</button>
                <h2>{arc.name}</h2>
                <p className={styles.arcDescription}>{arc.description}</p>

                <div className={styles.tasksSection}>
                    <h3>Ongoing Tasks</h3>
                    {ongoingTasks.length > 0 ? (
                        ongoingTasks.map(task => (
                            <div key={task.id} className={styles.taskItem}>
                                <div className={styles.taskInfo}>
                                    <span className={`${styles.taskType} ${styles[task.type.toLowerCase()]}`}>
                                        {task.type}
                                    </span>
                                    <p>{task.name}</p>
                                </div>
                                <button
                                    className={styles.completeButton}
                                    onClick={() => handleCompleteTask(task.id)}
                                >
                                    Complete (+{task.points} pts)
                                </button>
                            </div>
                        ))
                    ) : <p>All tasks for this arc are complete!</p>}
                </div>

                {completedTasks.length > 0 && (
                    <div className={styles.tasksSection}>
                        <h3>Completed Tasks</h3>
                        {completedTasks.map(task => (
                            <div key={task.id} className={`${styles.taskItem} ${styles.completed}`}>
                                <p><s>{task.name}</s></p>
                                <span>✅</span>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default TaskModal;