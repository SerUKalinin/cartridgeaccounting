import React from 'react';
import { Button, Box } from '@mui/material';

interface SwitchAuthModeButtonProps {
  onClick: () => void;
  children: React.ReactNode;
}

const SwitchAuthModeButton: React.FC<SwitchAuthModeButtonProps> = ({ onClick, children }) => (
  <Box sx={{ textAlign: 'center', mt: 2 }}>
    <Button onClick={onClick} fullWidth variant="text">
      {children}
    </Button>
  </Box>
);

export default SwitchAuthModeButton; 