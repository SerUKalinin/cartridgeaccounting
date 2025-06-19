import React, { useState, useCallback } from 'react';
import { Box, Button, Typography } from '@mui/material';
import OperationsTable from '../components/operations/OperationsTable';
import OperationFormDialog from '../components/operations/OperationFormDialog';

const OperationsPage: React.FC = () => {
  const [open, setOpen] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleSuccess = useCallback(() => {
    // Обновляем таблицу после успешного добавления
    setRefreshKey(prev => prev + 1);
  }, []);

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4">Операции</Typography>
        <Button variant="contained" onClick={handleOpen}>Добавить операцию</Button>
      </Box>
      <OperationsTable key={refreshKey} />
      <OperationFormDialog 
        open={open} 
        onClose={handleClose} 
        onSuccess={handleSuccess}
      />
    </Box>
  );
};

export default OperationsPage; 