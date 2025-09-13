// src/App.jsx
import { Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import HistoryPage from './pages/HistoryPage';
import MainLayout from './components/MainLayout';
import ProtectedRoute from './components/ProtectedRoute';
import SagasPage from './pages/SagasPage';
import TracksPage from './pages/TracksPage';
import ObjectivesPage from './pages/ObjectivesPage'

// Create placeholder pages for now
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
        <Route path="/sagas/:sagaId/tracks" element={<TracksPage />} />
        <Route path="/objectives" element={<ObjectivesPage />} />
        <Route path="/marketplace" element={<MarketplacePage />} />
        {/* The default protected route redirects to history */}
        <Route path="*" element={<HistoryPage />} />
      </Route>
    </Routes>
  );
}

export default App;