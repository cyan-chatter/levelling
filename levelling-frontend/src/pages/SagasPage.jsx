// src/pages/SagasPage.jsx
import React, { useState, useEffect } from 'react';
import api from '../services/api';
import SagaCard from '../components/SagaCard';
import styles from './SagasPage.module.css'; // Use CSS Modules

const SagasPage = () => {
    const [sagas, setSagas] = useState([]);

    useEffect(() => {
        api.get('/sagas')
            .then(response => {
                setSagas(response.data);
            })
            .catch(error => {
                console.error("Failed to fetch sagas:", error);
            });
    }, []);

    return (
        <div className={styles.sagasPage}>
            <h1 className={styles.sagasPageTitle}>Choose Your Saga</h1>
            {sagas.length > 0 ? (
                <div className={styles.sagasGrid}>
                    {sagas.map((saga, index) => (
                        <div
                            key={saga.id}
                            className={styles.cardEnter}
                            style={{ animationDelay: `${index * 0.1}s` }} // This creates the stagger
                        >
                            <SagaCard
                                id={saga.id}
                                name={saga.name}
                                description={saga.description}
                                imageUrl="/saga-placeholder.jpg"
                            />
                        </div>
                    ))}
                </div>
            ) : (
                <p>Loading Sagas...</p>
            )}
        </div>
    );
};

export default SagasPage;