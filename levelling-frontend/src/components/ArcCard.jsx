// src/components/ArcCard.jsx
import React from 'react';
import styles from './ArcCard.module.css';

const ArcCard = ({ arc, onSelect, canClaim, onClaim }) => {
    return (
        <div className={styles.arcCard} onClick={() => onSelect(arc)}>
            <div
                className={styles.arcImage}
                style={{ backgroundImage: `url(${arc.imageUrl || '/arc-placeholder.jpg'})` }}
            >
                <div className={styles.arcOverlay}>
                    <h3 className={styles.arcTitle}>{arc.name}</h3>
                    {/* --- NEW CLAIM BUTTON --- */}
                    {canClaim && (
                        <button
                            className={styles.claimButton}
                            onClick={(e) => {
                                e.stopPropagation(); // Prevent modal from opening
                                onClaim(arc);
                            }}
                        >
                            CLAIM
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ArcCard;