import React, { useMemo, useState } from 'react';
import { ThemeProvider, createTheme, CssBaseline, Box, AppBar, Toolbar, Typography, Container } from '@mui/material';
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import ThemeToggle from './components/ThemeToggle';

const PrivateRoute: React.FC = () => {
  const token = localStorage.getItem('token') || sessionStorage.getItem('token');
  return token ? <Outlet /> : <Navigate to="/login" replace />;
};

const App: React.FC = () => {
  const [mode, setMode] = useState<'light' | 'dark'>('dark');
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

  const toggleTheme = () => setMode(prev => (prev === 'dark' ? 'light' : 'dark'));

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <AppBar position="static" color="primary" enableColorOnDark>
          <Toolbar>
            <Typography variant="h6" sx={{ flexGrow: 1 }}>
              Учёт картриджей МФУ
            </Typography>
            <ThemeToggle toggleTheme={toggleTheme} />
          </Toolbar>
        </AppBar>
        <Box sx={{ minHeight: 'calc(100vh - 64px)', bgcolor: 'background.default' }}>
          <Container maxWidth="sm" sx={{ py: 4 }}>
            <Routes>
              <Route path="/login" element={<LoginPage />} />
              <Route element={<PrivateRoute />}>
                <Route path="/dashboard" element={<DashboardPage />} />
              </Route>
              <Route path="*" element={<Navigate to="/login" />} />
            </Routes>
          </Container>
        </Box>
      </Router>
    </ThemeProvider>
  );
};

export default App;
