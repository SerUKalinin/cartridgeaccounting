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
import { CartridgeDto } from '../../api/cartridges';

interface DeleteCartridgeDialogProps {
  open: boolean;
  cartridge: CartridgeDto | null;
  onClose: () => void;
  onConfirm: () => void;
  loading?: boolean;
}

const DeleteCartridgeDialog: React.FC<DeleteCartridgeDialogProps> = ({
  open,
  cartridge,
  onClose,
  onConfirm,
  loading = false
}) => {
  if (!cartridge) return null;

  const getStatusLabel = (status: string) => {
    const statusMap: { [key: string]: string } = {
      'IN_STOCK': 'На складе',
      'IN_USE': 'В использовании',
      'REFILLING': 'На заправке',
      'DISPOSED': 'Списан'
    };
    return statusMap[status] || status;
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <WarningIcon color="error" />
        Подтверждение удаления
      </DialogTitle>
      <DialogContent>
        <DialogContentText>
          Вы действительно хотите удалить картридж?
        </DialogContentText>
        <Box sx={{ mt: 2, p: 2, bgcolor: 'grey.50', borderRadius: 1 }}>
          <Typography variant="subtitle2" gutterBottom>
            Информация о картридже:
          </Typography>
          <Typography variant="body2">
            <strong>Модель:</strong> {cartridge.model}
          </Typography>
          <Typography variant="body2">
            <strong>Серийный номер:</strong> {cartridge.serialNumber || 'Не указан'}
          </Typography>
          <Typography variant="body2">
            <strong>Статус:</strong> {getStatusLabel(cartridge.status)}
          </Typography>
          <Typography variant="body2">
            <strong>Ресурс:</strong> {cartridge.resourcePages ? `${cartridge.resourcePages} стр.` : 'Не указан'}
          </Typography>
          <Typography variant="body2">
            <strong>Местоположение:</strong> {cartridge.currentLocationName || 'Не указано'}
          </Typography>
          <Typography variant="body2">
            <strong>Дата создания:</strong> {new Date(cartridge.createdAt).toLocaleDateString('ru-RU')}
          </Typography>
        </Box>
        <DialogContentText sx={{ mt: 2, color: 'error.main' }}>
          <strong>Внимание:</strong> Это действие нельзя отменить. Все данные картриджа будут удалены безвозвратно.
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

export default DeleteCartridgeDialog; 