import React, { useState } from 'react';
import { Box, Button, Typography, Alert, Snackbar } from '@mui/material';
import { Add as AddIcon } from '@mui/icons-material';
import UsersTable from '../components/users/UsersTable';
import UserFormDialog from '../components/users/UserFormDialog';
import DeleteUserDialog from '../components/users/DeleteUserDialog';
import { UserDto, deleteUser } from '../api/users';

const UsersPage: React.FC = () => {
  const [openAddDialog, setOpenAddDialog] = useState(false);
  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [selectedUser, setSelectedUser] = useState<UserDto | null>(null);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [snackbar, setSnackbar] = useState<{
    open: boolean;
    message: string;
    severity: 'success' | 'error';
  }>({ open: false, message: '', severity: 'success' });

  const handleOpenAdd = () => setOpenAddDialog(true);
  const handleCloseAdd = () => setOpenAddDialog(false);

  const handleEditUser = (user: UserDto) => {
    setSelectedUser(user);
    setOpenEditDialog(true);
  };

  const handleCloseEdit = () => {
    setOpenEditDialog(false);
    setSelectedUser(null);
  };

  const handleDeleteUser = (user: UserDto) => {
    setSelectedUser(user);
    setOpenDeleteDialog(true);
  };

  const handleCloseDelete = () => {
    setOpenDeleteDialog(false);
    setSelectedUser(null);
  };

  const handleConfirmDelete = async () => {
    if (!selectedUser) return;

    try {
      setDeleteLoading(true);
      await deleteUser(selectedUser.id);
      setSnackbar({
        open: true,
        message: 'Пользователь успешно удален',
        severity: 'success'
      });
      handleCloseDelete();
      // Перезагружаем таблицу
      window.location.reload();
    } catch (error) {
      console.error('Error deleting user:', error);
      setSnackbar({
        open: true,
        message: 'Ошибка при удалении пользователя',
        severity: 'error'
      });
    } finally {
      setDeleteLoading(false);
    }
  };

  const handleSuccess = (message: string) => {
    setSnackbar({
      open: true,
      message,
      severity: 'success'
    });
    handleCloseAdd();
    handleCloseEdit();
    // Перезагружаем таблицу
    window.location.reload();
  };

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4">Пользователи</Typography>
        <Button 
          variant="contained" 
          onClick={handleOpenAdd}
          startIcon={<AddIcon />}
        >
          Добавить пользователя
        </Button>
      </Box>
      
      <UsersTable 
        onEditUser={handleEditUser}
        onDeleteUser={handleDeleteUser}
      />
      
      {/* Диалог добавления пользователя */}
      <UserFormDialog 
        open={openAddDialog} 
        onClose={handleCloseAdd}
        onSuccess={() => handleSuccess('Пользователь успешно создан')}
      />
      
      {/* Диалог редактирования пользователя */}
      <UserFormDialog 
        open={openEditDialog} 
        onClose={handleCloseEdit}
        user={selectedUser}
        onSuccess={() => handleSuccess('Пользователь успешно обновлен')}
      />
      
      {/* Диалог подтверждения удаления */}
      <DeleteUserDialog
        open={openDeleteDialog}
        user={selectedUser}
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

export default UsersPage; 