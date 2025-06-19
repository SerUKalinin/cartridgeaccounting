import React from 'react';
import { Drawer, List, ListItem, ListItemIcon, ListItemText, ListItemButton, Divider } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';
import DashboardIcon from '@mui/icons-material/Dashboard';
import PeopleIcon from '@mui/icons-material/People';
import PrintIcon from '@mui/icons-material/Print';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import HistoryIcon from '@mui/icons-material/History';
import AssessmentIcon from '@mui/icons-material/Assessment';

interface AppDrawerProps {
  open: boolean;
  onClose: () => void;
}

const menuItems = [
  { text: 'Дашборд', icon: <DashboardIcon />, path: '/dashboard' },
  { text: 'Пользователи', icon: <PeopleIcon />, path: '/users' },
  { text: 'Картриджи', icon: <PrintIcon />, path: '/cartridges' },
  { text: 'Объекты', icon: <LocationOnIcon />, path: '/locations' },
  { text: 'Операции', icon: <HistoryIcon />, path: '/operations' },
  { text: 'Отчёты', icon: <AssessmentIcon />, path: '/reports' },
];

const AppDrawer: React.FC<AppDrawerProps> = ({ open, onClose }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleNavigation = (path: string) => {
    navigate(path);
    onClose();
  };

  return (
    <Drawer
      variant="temporary"
      open={open}
      onClose={onClose}
      ModalProps={{ keepMounted: true }}
      sx={{
        display: { xs: 'block' },
        '& .MuiDrawer-paper': { 
          boxSizing: 'border-box', 
          width: 240,
          zIndex: (theme) => theme.zIndex.drawer,
          mt: '64px', // Отступ сверху равный высоте AppBar
        },
      }}
    >
      <List>
        {menuItems.map((item) => (
          <ListItem key={item.text} disablePadding>
            <ListItemButton
              selected={location.pathname === item.path}
              onClick={() => handleNavigation(item.path)}
            >
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Drawer>
  );
};

export default AppDrawer; 