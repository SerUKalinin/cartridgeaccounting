import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Alert
} from '@mui/material';
import { LocationDto } from '../../api/locations';

interface DeleteLocationDialogProps {
  open: boolean;
  location: LocationDto | null;
  onClose: () => void;
  onConfirm: () => void;
  loading?: boolean;
  error?: string | null;
}

const DeleteLocationDialog: React.FC<DeleteLocationDialogProps> = ({
  open,
  location,
  onClose,
  onConfirm,
  loading = false,
  error = null
}) => {
  if (!location) return null;

  // Проверяем, содержит ли ошибка информацию о связанных картриджах
  const hasCartridgesError = error && error.includes('связанных картриджей');

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Удаление объекта</DialogTitle>
      <DialogContent>
        {error && (
          <Alert severity={hasCartridgesError ? "warning" : "error"} sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}
        <Typography>
          Вы действительно хотите удалить объект <strong>"{location.name}"</strong>?
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
          Адрес: {location.address}
        </Typography>
        {!hasCartridgesError && (
          <Typography variant="body2" color="error" sx={{ mt: 2 }}>
            Внимание: Это действие нельзя отменить. Все связанные данные также будут удалены.
          </Typography>
        )}
        {hasCartridgesError && (
          <Typography variant="body2" color="warning.main" sx={{ mt: 2 }}>
            Для удаления объекта сначала переместите или удалите все картриджи, находящиеся на этом объекте.
          </Typography>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={loading}>
          Отмена
        </Button>
        {!hasCartridgesError && (
          <Button 
            onClick={onConfirm} 
            color="error" 
            variant="contained"
            disabled={loading}
          >
            Удалить
          </Button>
        )}
      </DialogActions>
    </Dialog>
  );
};

export default DeleteLocationDialog; 