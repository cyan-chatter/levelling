// src/components/MainLayout.jsx
import React from 'react';
import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './MainLayout.css';

const TopBar = () => {
    const { user, logout } = useAuth();
    const currencies = user?.currencies;

    return (
        <div className="top-bar">
            <div className="currency-meters">
                <span title="Points">
                    {currencies?.points?.toLocaleString() ?? 0} 🪙
                </span>
                <span title="Happiness Gems (Daily)">
                    🟡 {currencies?.yellowGems ?? 0}
                </span>
                <span title="Effort Gems (Daily)">
                    🔵 {currencies?.royalBlueGems ?? 0}
                </span>
                <span title="Regularity Gems (Weekly)">
                    🔴 {currencies?.crimsonRedGems ?? 0}
                </span>
                <span title="Skill Gems (Monthly)">
                    🟢 {currencies?.emeraldGreenGems ?? 0}
                </span>
            </div>
            {/* We add a class name for styling */}
            <button className="logout-button" onClick={logout}>
                Logout
            </button>
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
                    <Outlet />
                </main>
            </div>
        </div>
    );
};

export default MainLayout;