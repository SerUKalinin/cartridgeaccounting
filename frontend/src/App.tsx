import React, { useMemo, useState } from 'react';
import { ThemeProvider, createTheme, CssBaseline, Box } from '@mui/material';
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import UsersPage from './pages/UsersPage';
import CartridgesPage from './pages/CartridgesPage';
import LocationsPage from './pages/LocationsPage';
import OperationsPage from './pages/OperationsPage';
import ReportsPage from './pages/ReportsPage';
import AppLayout from './components/layout/AppLayout';

// Компонент для проверки авторизации
const PrivateRoute: React.FC = () => {
  const token = localStorage.getItem('token') || sessionStorage.getItem('token');
  
  if (!token) {
    // Сохраняем текущий путь для редиректа после входа
    const currentPath = window.location.pathname;
    if (currentPath !== '/login') {
      localStorage.setItem('redirectPath', currentPath);
    }
    return <Navigate to="/login" replace />;
  }
  
  // Проверяем, не истек ли токен (базовая проверка)
  try {
    const tokenData = JSON.parse(atob(token.split('.')[1]));
    const currentTime = Date.now() / 1000;
    
    if (tokenData.exp && tokenData.exp < currentTime) {
      // Токен истек
      localStorage.removeItem('token');
      sessionStorage.removeItem('token');
      localStorage.removeItem('role');
      sessionStorage.removeItem('role');
      const currentPath = window.location.pathname;
      if (currentPath !== '/login') {
        localStorage.setItem('redirectPath', currentPath);
      }
      return <Navigate to="/login" replace />;
    }
  } catch (error) {
    // Если токен поврежден, очищаем его
    localStorage.removeItem('token');
    sessionStorage.removeItem('token');
    localStorage.removeItem('role');
    sessionStorage.removeItem('role');
    const currentPath = window.location.pathname;
    if (currentPath !== '/login') {
      localStorage.setItem('redirectPath', currentPath);
    }
    return <Navigate to="/login" replace />;
  }
  
  return <Outlet />;
};

const App: React.FC = () => {
  const [mode, setMode] = useState<'light' | 'dark'>('light');
  const theme = useMemo(() => createTheme({
    palette: {
      mode,
      primary: {
        main: '#1976d2',
      },
      background: {
        default: mode === 'dark' ? '#121212' : '#f4f6fa',
        paper: mode === 'dark' ? '#1e1e1e' : '#fff',
      },
    },
    shape: { borderRadius: 12 },
  }), [mode]);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Box sx={{ minHeight: '100vh', bgcolor: 'background.default' }}>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route element={<PrivateRoute />}>
              <Route element={<AppLayout />}>
                <Route path="/" element={<Navigate to="/dashboard" replace />} />
                <Route path="/dashboard" element={<DashboardPage />} />
                <Route path="/users" element={<UsersPage />} />
                <Route path="/cartridges" element={<CartridgesPage />} />
                <Route path="/locations" element={<LocationsPage />} />
                <Route path="/operations" element={<OperationsPage />} />
                <Route path="/reports" element={<ReportsPage />} />
              </Route>
            </Route>
            <Route path="*" element={<Navigate to="/login" />} />
          </Routes>
        </Box>
      </Router>
    </ThemeProvider>
  );
};

export default App;
