// src/components/TrackSidebarItem.jsx
import React from 'react';
import styles from './TrackSidebarItem.module.css';

const TrackSidebarItem = ({ track, onSelect, isActive }) => {
    // Combine class names conditionally
    const itemClasses = `${styles.sidebarItem} ${isActive ? styles.active : ''}`;

    return (
        <div className={itemClasses} onClick={() => onSelect(track)}>
            <img src="/track-icon.png" alt={track.name} className={styles.sidebarIcon} />
            <span className={styles.sidebarName}>{track.name}</span>
        </div>
    );
};

export default TrackSidebarItem;