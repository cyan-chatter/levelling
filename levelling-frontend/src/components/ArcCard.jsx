// src/components/ArcCard.jsx
import React from 'react';
import styles from './ArcCard.module.css';

const ArcCard = ({ arc, onSelect }) => {
    return (
        <div className={styles.arcCard} onClick={() => onSelect(arc)}>
            <div
                className={styles.arcImage}
                style={{ backgroundImage: `url(${arc.imageUrl || '/arc-placeholder.jpg'})` }}
            >
                <div className={styles.arcOverlay}>
                    <h3 className={styles.arcTitle}>{arc.name}</h3>
                </div>
            </div>
        </div>
    );
};

export default ArcCard;