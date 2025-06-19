import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { Box } from '@mui/material';
import AppBar from './AppBar';
import AppDrawer from './AppDrawer';

const AppLayout: React.FC = () => {
  const [drawerOpen, setDrawerOpen] = useState(false);

  const handleDrawerToggle = () => {
    setDrawerOpen(!drawerOpen);
  };

  return (
    <Box sx={{ display: 'flex' }}>
      <AppBar onMenuClick={handleDrawerToggle} />
      <AppDrawer open={drawerOpen} onClose={handleDrawerToggle} />
      <Box 
        component="main" 
        sx={{ 
          flexGrow: 1, 
          p: 3, 
          mt: '64px', // Отступ сверху равный высоте AppBar
          minHeight: 'calc(100vh - 64px)', // Минимальная высота с учетом AppBar
        }}
      >
        <Outlet />
      </Box>
    </Box>
  );
};

export default AppLayout; 