// src/pages/HistoryPage.jsx

import React, { useState, useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
import { useGSAP } from '@gsap/react';
import { format, parseISO, isSameDay, startOfMonth, getYear, formatISO } from 'date-fns';
import api from '../services/api';
import './HistoryPage.css'; // We will create this file next

gsap.registerPlugin(ScrollTrigger, useGSAP);

const HistoryPage = () => {
    const [history, setHistory] = useState({});
    const [activeDate, setActiveDate] = useState(formatISO(new Date(), { representation: 'date' }));
    const [currentMonthYear, setCurrentMonthYear] = useState('');

    const mainContainerRef = useRef(null);
    const timelineRef = useRef(null);

    // 1. Fetch and process history data
    useEffect(() => {
        const fetchHistory = async () => {
            try {
                const response = await api.get('/users/me/history');
                const events = response.data;

                // Group events by date
                const grouped = events.reduce((acc, event) => {
                    const date = formatISO(parseISO(event.completedAt), { representation: 'date' });
                    if (!acc[date]) {
                        acc[date] = [];
                    }
                    acc[date].push(event);
                    return acc;
                }, {});
                setHistory(grouped);
            } catch (error) {
                console.error("Failed to fetch history:", error);
            }
        };
        fetchHistory();
    }, []);

    // 2. GSAP Animation Logic
    useGSAP(() => {
        if (Object.keys(history).length === 0) return;

        const circles = gsap.utils.toArray('.timeline-circle');

        circles.forEach(circle => {
            // Animate circles scaling up and down based on scroll position
            gsap.to(circle, {
                scale: 1.5,
                scrollTrigger: {
                    trigger: circle,
                    scroller: mainContainerRef.current, // Use our main div as the scroller
                    start: 'top center',
                    end: 'bottom center',
                    scrub: 0.5,
                }
            });

            // Update the active date and month/year when a circle enters the center
            ScrollTrigger.create({
                trigger: circle,
                scroller: mainContainerRef.current,
                start: 'top center',
                end: 'bottom center',
                onEnter: () => updateActiveInfo(circle.dataset.date),
                onEnterBack: () => updateActiveInfo(circle.dataset.date),
            });
        });

    }, { scope: timelineRef, dependencies: [history] });

    const updateActiveInfo = (dateStr) => {
        setActiveDate(dateStr);
        const date = parseISO(dateStr);
        setCurrentMonthYear(format(date, 'MMMM yyyy'));
    };

    const sortedDates = Object.keys(history).sort((a, b) => new Date(b) - new Date(a));

    return (
        <div className="history-container" ref={mainContainerRef}>
            <header className="history-header">
                {currentMonthYear}
            </header>

            <div className="history-content">
                <div className="events-panel">
                    <h2>{format(parseISO(activeDate), 'EEEE, do')}</h2>
                    <div className="events-list">
                        {history[activeDate] ? (
                            history[activeDate].map((event, index) => (
                                <div key={index} className="event-card">
                                    <span className={`event-type ${event.type.toLowerCase()}`}>{event.type}</span>
                                    <p className="event-name">{event.name}</p>
                                    <span className="event-points">+{event.points} pts</span>
                                </div>
                            ))
                        ) : (
                            <p>No events for this day.</p>
                        )}
                    </div>
                </div>

                <div className="timeline-panel" ref={timelineRef}>
                    <div className="timeline-bar"></div>
                    {sortedDates.map(dateStr => (
                        <div
                            key={dateStr}
                            className="timeline-circle"
                            data-date={dateStr}
                        >
                            {format(parseISO(dateStr), 'd')}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default HistoryPage;