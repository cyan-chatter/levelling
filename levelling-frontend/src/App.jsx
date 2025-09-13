// src/App.jsx
import { Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import HistoryPage from './pages/HistoryPage';
import MainLayout from './components/MainLayout';
import ProtectedRoute from './components/ProtectedRoute';

// Create placeholder pages for now
const SagasPage = () => <div>Sagas Page</div>;
const ObjectivesPage = () => <div>Objectives Page</div>;
const MarketplacePage = () => <div>Marketplace Page</div>;

function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />

      {/* All protected routes will go inside this MainLayout route */}
      <Route
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
        <Route path="/history" element={<HistoryPage />} />
        <Route path="/sagas" element={<SagasPage />} />
        <Route path="/objectives" element={<ObjectivesPage />} />
        <Route path="/marketplace" element={<MarketplacePage />} />
        {/* The default protected route redirects to history */}
        <Route path="*" element={<HistoryPage />} />
      </Route>
    </Routes>
  );
}

export default App;