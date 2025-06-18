import React from 'react';
import { Typography, Box } from '@mui/material';

const DashboardPage: React.FC = () => {
  return (
    <Box sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Добро пожаловать в систему учёта картриджей МФУ!
      </Typography>
      <Typography variant="body1">
        Здесь будет основной интерфейс приложения (dashboard).
      </Typography>
    </Box>
  );
};

export default DashboardPage; 