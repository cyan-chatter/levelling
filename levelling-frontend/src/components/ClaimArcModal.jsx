// src/components/ClaimArcModal.jsx
import React, { useState } from 'react';
import styles from './ObjectiveSelectionModal.module.css'; // Reuse styles

const ClaimArcModal = ({ arc, onClaim, onCancel }) => {
    const [happiness, setHappiness] = useState(0);
    const [effort, setEffort] = useState(0);

    return (
        <div className={styles.modalBackdrop} onClick={onCancel}>
            <div className={styles.modalContent} onClick={e => e.stopPropagation()}>
                <h2>Claim Rewards for {arc.name}</h2>
                <p>You've completed all primary tasks! Rate your experience to claim your rewards.</p>

                <div className={styles.sliderGroup}>
                    <label>Happiness Level: {happiness}</label>
                    <input type="range" min="-10" max="10" value={happiness} onChange={e => setHappiness(e.target.value)} />
                </div>
                <div className={styles.sliderGroup}>
                    <label>Effort Level: {effort}</label>
                    <input type="range" min="-10" max="10" value={effort} onChange={e => setEffort(e.target.value)} />
                </div>

                <div className={styles.modalActions}>
                    <button onClick={onCancel} className={styles.cancelButton}>Cancel</button>
                    <button onClick={() => onClaim({ happinessLevel: happiness, effortLevel: effort })} className={styles.saveButton}>
                        Claim +{arc.points} pts
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ClaimArcModal;