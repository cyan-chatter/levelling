// src/context/AuthContext.jsx
import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('accessToken'));
    const navigate = useNavigate();

    useEffect(() => {
        if (token) {
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            // Fetch user data if we have a token but no user object
            api.get('/users/me')
                .then(response => setUser(response.data))
                .catch(() => {
                    // Token is invalid
                    logout();
                });
        }
    }, [token]);

    const login = async (usernameOrEmail, password) => {
        try {
            const response = await api.post('/auth/login', { usernameOrEmail, password });
            const { accessToken } = response.data;
            localStorage.setItem('accessToken', accessToken);
            setToken(accessToken);
            // We don't navigate here; the component calling login will.
            return true;
        } catch (error) {
            console.error("Login failed:", error);
            return false;
        }
    };

    const logout = () => {
        setUser(null);
        setToken(null);
        localStorage.removeItem('accessToken');
        navigate('/login'); // Redirect to login on logout
    };

    const value = { user, token, login, logout };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// Custom hook to easily use the auth context
export const useAuth = () => {
    return useContext(AuthContext);
};