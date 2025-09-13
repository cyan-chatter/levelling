// src/pages/LoginPage.jsx
import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { gsap } from 'gsap';
import { useGSAP } from '@gsap/react';
import './LoginPage.css'; // We'll create this

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();
    const container = useRef();

    useGSAP(() => {
        gsap.from(container.current, { duration: 1, opacity: 0, ease: 'power2.inOut' });
    }, { scope: container });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        const success = await login(username, password);
        if (success) {
            navigate('/history'); // Navigate to History page on successful login
        } else {
            setError('Login failed. Please check your credentials.');
        }
    };

    return (
        <div className="login-container" ref={container}>
            <div className="hero-section">
                <h1>LEVELLING</h1>
                <p>Track your progress. Gamify your life.</p>
            </div>
            <div className="login-form-section">
                <form onSubmit={handleSubmit}>
                    <h2>Enter the Game</h2>
                    <input
                        type="text"
                        placeholder="Username or Email"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    {error && <p className="error-message">{error}</p>}
                    <button type="submit">Login</button>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;