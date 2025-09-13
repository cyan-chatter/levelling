// src/components/MainLayout.jsx
import React from 'react';
import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './MainLayout.css'; // We'll create this

const TopBar = () => {
    const { user, logout } = useAuth();
    // In a real app, you'd fetch currencies and display them here
    return (
        <div className="top-bar">
            <div className="currency-meters">
                <span>Points: {user?.currencies?.points ?? 0}</span>
                <span>💎: {user?.currencies?.yellowGems ?? 0}</span>
            </div>
            <button onClick={logout}>Logout</button>
        </div>
    );
};

const Navbar = () => {
    return (
        <nav className="main-nav">
            <Link to="/history">History</Link>
            <Link to="/sagas">Sagas</Link>
            <Link to="/objectives">Objectives</Link>
            <Link to="/marketplace">Marketplace</Link>
        </nav>
    );
};

const MainLayout = () => {
    return (
        <div className="app-container">
            <Navbar />
            <div className="content-container">
                <TopBar />
                <main className="page-content">
                    {/* This is where the routed page component will be rendered */}
                    <Outlet />
                </main>
            </div>
        </div>
    );
};

export default MainLayout;