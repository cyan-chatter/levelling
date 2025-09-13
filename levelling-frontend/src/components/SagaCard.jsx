// src/components/SagaCard.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import styles from './SagaCard.module.css';

const SagaCard = ({ id, name, description, imageUrl }) => {
    return (
        <Link to={`/sagas/${id}/tracks`} className={styles.sagaCard}>
            <div className={styles.sagaCardImage} style={{ backgroundImage: `url(${imageUrl})` }}></div>
            <div className={styles.sagaCardContent}>
                <h2 className={styles.sagaCardTitle}>{name}</h2>
                <p className={styles.sagaCardDescription}>{description}</p>
            </div>
        </Link>
    );
};

export default SagaCard;