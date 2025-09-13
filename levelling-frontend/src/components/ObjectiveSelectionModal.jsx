// src/components/ObjectiveSelectionModal.jsx
import React, { useState, useEffect, useMemo } from 'react';
import api from '../services/api';
import styles from './ObjectiveSelectionModal.module.css';

const ObjectiveSelectionModal = ({ objectiveType, onClose, onSave }) => {
    const [allItems, setAllItems] = useState([]);
    const [selectedIds, setSelectedIds] = useState(new Set());
    const [isLoading, setIsLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const endpoint = objectiveType === 'daily' ? '/content/tasks/all-preset' : '/content/arcs/all-preset';
        api.get(endpoint)
            .then(response => setAllItems(response.data))
            .catch(error => console.error(`Failed to fetch items:`, error))
            .finally(() => setIsLoading(false));
    }, [objectiveType]);

    // This is where the filtering magic happens!
    const filteredItems = useMemo(() => {
        if (!searchTerm) return allItems;

        const lowerCaseSearch = searchTerm.toLowerCase();

        // Parse search tags like "arc:html"
        const tags = {};
        const generalSearchTerms = lowerCaseSearch.replace(/arc:"([^"]+)"|arc:(\S+)|track:"([^"]+)"|track:(\S+)/g,
            (match, arcQuote, arcWord, trackQuote, trackWord) => {
                if (arcQuote || arcWord) tags.arc = (arcQuote || arcWord).toLowerCase();
                // Add track logic later if needed
                return '';
            }).trim().split(' ').filter(Boolean);

        return allItems.filter(item => {
            // Check if item matches all specified tags
            if (tags.arc && !item.arcName?.toLowerCase().includes(tags.arc)) {
                return false;
            }
            // Add track check later if needed

            // Check if item matches all general search terms
            const itemName = item.name.toLowerCase();
            return generalSearchTerms.every(term => itemName.includes(term));
        });

    }, [searchTerm, allItems]);

    const handleSelect = (itemId) => {
        setSelectedIds(prev => {
            const newSet = new Set(prev);
            if (newSet.has(itemId)) newSet.delete(itemId);
            else newSet.add(itemId);
            return newSet;
        });
    };

    const handleSave = () => onSave(Array.from(selectedIds));

    return (
        <div className={styles.modalBackdrop} onClick={onClose}>
            <div className={styles.modalContent} onClick={e => e.stopPropagation()}>
                <h2>Select Your {objectiveType === 'daily' ? 'Daily' : 'Weekly'} Objectives</h2>

                <input
                    type="text"
                    placeholder='Search... e.g., landing page arc:"html-css-js"'
                    className={styles.searchInput}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />

                <div className={styles.itemList}>
                    {isLoading ? <p>Loading...</p> : (
                        filteredItems.map(item => (
                            <div
                                key={item.id}
                                className={`${styles.item} ${selectedIds.has(item.id) ? styles.selected : ''}`}
                                onClick={() => handleSelect(item.id)}
                            >
                                <div>
                                    <p className={styles.itemName}>{item.name}</p>
                                    {item.arcName && <span className={styles.itemSubtext}>From Arc: {item.arcName}</span>}
                                </div>
                                <span>+{item.points} pts</span>
                            </div>
                        ))
                    )}
                </div>
                <div className={styles.modalActions}>
                    <button onClick={onClose} className={styles.cancelButton}>Cancel</button>
                    <button onClick={handleSave} className={styles.saveButton}>Save Objectives</button>
                </div>
            </div>
        </div>
    );
};

export default ObjectiveSelectionModal;