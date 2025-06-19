import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
  Box,
  Typography
} from '@mui/material';
import { Warning as WarningIcon } from '@mui/icons-material';
import { UserDto } from '../../api/users';
import { translateRole } from '../../utils/roleTranslations';

interface DeleteUserDialogProps {
  open: boolean;
  user: UserDto | null;
  onClose: () => void;
  onConfirm: () => void;
  loading?: boolean;
}

const DeleteUserDialog: React.FC<DeleteUserDialogProps> = ({
  open,
  user,
  onClose,
  onConfirm,
  loading = false
}) => {
  if (!user) return null;

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <WarningIcon color="error" />
        Подтверждение удаления
      </DialogTitle>
      <DialogContent>
        <DialogContentText>
          Вы действительно хотите удалить пользователя?
        </DialogContentText>
        <Box sx={{ mt: 2, p: 2, bgcolor: 'grey.50', borderRadius: 1 }}>
          <Typography variant="subtitle2" gutterBottom>
            Информация о пользователе:
          </Typography>
          <Typography variant="body2">
            <strong>Имя пользователя:</strong> {user.username}
          </Typography>
          <Typography variant="body2">
            <strong>Полное имя:</strong> {user.fullName}
          </Typography>
          <Typography variant="body2">
            <strong>Роль:</strong> {translateRole(user.role)}
          </Typography>
          <Typography variant="body2">
            <strong>Статус:</strong> {user.enabled ? 'Активен' : 'Неактивен'}
          </Typography>
        </Box>
        <DialogContentText sx={{ mt: 2, color: 'error.main' }}>
          <strong>Внимание:</strong> Это действие нельзя отменить. Все данные пользователя будут удалены безвозвратно.
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={loading}>
          Отмена
        </Button>
        <Button 
          onClick={onConfirm} 
          color="error" 
          variant="contained"
          disabled={loading}
        >
          {loading ? 'Удаление...' : 'Удалить'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DeleteUserDialog; 