import React, { useState, useCallback } from 'react';
import { Box, Button, Typography, Alert, Snackbar } from '@mui/material';
import { Add as AddIcon } from '@mui/icons-material';
import CartridgesTable from '../components/cartridges/CartridgesTable';
import CartridgeFormDialog from '../components/cartridges/CartridgeFormDialog';
import DeleteCartridgeDialog from '../components/cartridges/DeleteCartridgeDialog';
import { CartridgeDto, deleteCartridge } from '../api/cartridges';
import { useLocation } from 'react-router-dom';

const CartridgesPage: React.FC = () => {
  const [openAddDialog, setOpenAddDialog] = useState(false);
  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [selectedCartridge, setSelectedCartridge] = useState<CartridgeDto | null>(null);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);
  const [snackbar, setSnackbar] = useState<{
    open: boolean;
    message: string;
    severity: 'success' | 'error';
  }>({ open: false, message: '', severity: 'success' });
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const initialStatus = searchParams.get('status') || '';

  const handleOpenAdd = () => setOpenAddDialog(true);
  const handleCloseAdd = () => setOpenAddDialog(false);

  const handleEditCartridge = (cartridge: CartridgeDto) => {
    setSelectedCartridge(cartridge);
    setOpenEditDialog(true);
  };

  const handleCloseEdit = () => {
    setOpenEditDialog(false);
    setSelectedCartridge(null);
  };

  const handleDeleteCartridge = (cartridge: CartridgeDto) => {
    setSelectedCartridge(cartridge);
    setOpenDeleteDialog(true);
  };

  const handleCloseDelete = () => {
    setOpenDeleteDialog(false);
    setSelectedCartridge(null);
  };

  const handleConfirmDelete = async () => {
    if (!selectedCartridge) return;

    try {
      setDeleteLoading(true);
      await deleteCartridge(selectedCartridge.id);
      setSnackbar({
        open: true,
        message: 'Картридж успешно удален',
        severity: 'success'
      });
      handleCloseDelete();
      // Перезагружаем таблицу
      setRefreshKey(prev => prev + 1);
    } catch (error) {
      console.error('Error deleting cartridge:', error);
      setSnackbar({
        open: true,
        message: 'Ошибка при удалении картриджа',
        severity: 'error'
      });
    } finally {
      setDeleteLoading(false);
    }
  };

  const handleSuccess = useCallback((message: string) => {
    setSnackbar({
      open: true,
      message,
      severity: 'success'
    });
    handleCloseAdd();
    handleCloseEdit();
    // Перезагружаем таблицу
    setRefreshKey(prev => prev + 1);
  }, []);

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4">Картриджи</Typography>
        <Button 
          variant="contained" 
          onClick={handleOpenAdd}
          startIcon={<AddIcon />}
        >
          Добавить картридж
        </Button>
      </Box>
      
      <CartridgesTable 
        key={refreshKey}
        onEditCartridge={handleEditCartridge}
        onDeleteCartridge={handleDeleteCartridge}
        initialStatus={initialStatus}
      />
      
      {/* Диалог добавления картриджа */}
      <CartridgeFormDialog 
        open={openAddDialog} 
        onClose={handleCloseAdd} 
        onSuccess={() => handleSuccess('Картридж успешно создан')}
      />
      
      {/* Диалог редактирования картриджа */}
      <CartridgeFormDialog 
        open={openEditDialog} 
        onClose={handleCloseEdit}
        cartridge={selectedCartridge}
        onSuccess={() => handleSuccess('Картридж успешно обновлен')}
      />
      
      {/* Диалог подтверждения удаления */}
      <DeleteCartridgeDialog
        open={openDeleteDialog}
        cartridge={selectedCartridge}
        onClose={handleCloseDelete}
        onConfirm={handleConfirmDelete}
        loading={deleteLoading}
      />
      
      {/* Уведомления */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
      >
        <Alert 
          onClose={handleCloseSnackbar} 
          severity={snackbar.severity}
          sx={{ width: '100%' }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default CartridgesPage; 