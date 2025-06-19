import React from 'react';
import { AppBar as MuiAppBar, Toolbar, Typography, IconButton, Box } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate } from 'react-router-dom';

interface AppBarProps {
  onMenuClick: () => void;
}

const AppBar: React.FC<AppBarProps> = ({ onMenuClick }) => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    sessionStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <MuiAppBar 
      position="fixed" 
      sx={{ 
        zIndex: (theme) => theme.zIndex.drawer + 1,
        height: '64px', // Фиксированная высота AppBar
      }}
    >
      <Toolbar sx={{ height: '64px' }}>
        <IconButton
          color="inherit"
          aria-label="open drawer"
          edge="start"
          onClick={onMenuClick}
          sx={{ mr: 2 }}
        >
          <MenuIcon />
        </IconButton>
        <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
          Учёт картриджей МФУ
        </Typography>
        <Box>
          <IconButton color="inherit" onClick={handleLogout}>
            <LogoutIcon />
          </IconButton>
        </Box>
      </Toolbar>
    </MuiAppBar>
  );
};

export default AppBar; 