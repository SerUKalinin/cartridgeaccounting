import React, { useState, useEffect } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, MenuItem, CircularProgress, Alert } from '@mui/material';
import { createOperation, CreateOperationRequest } from '../../api/operations';
import { getCartridges, CartridgeDto } from '../../api/cartridges';
import { getActiveLocations, LocationDto } from '../../api/locations';

const operationTypes = [
  { value: 'RECEIPT', label: 'Поступление' },
  { value: 'ISSUE', label: 'Выдача' },
  { value: 'RETURN', label: 'Возврат' },
  { value: 'REFILL', label: 'Заправка' },
  { value: 'DISPOSAL', label: 'Списание' },
];

interface OperationFormDialogProps {
  open: boolean;
  onClose: () => void;
  onSuccess?: () => void;
}

const OperationFormDialog: React.FC<OperationFormDialogProps> = ({ open, onClose, onSuccess }) => {
  const [type, setType] = useState('RECEIPT');
  const [cartridgeId, setCartridgeId] = useState('');
  const [locationId, setLocationId] = useState('');
  const [count, setCount] = useState('1');
  const [notes, setNotes] = useState('');
  const [cartridges, setCartridges] = useState<CartridgeDto[]>([]);
  const [locations, setLocations] = useState<LocationDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Загрузка данных при открытии диалога
  useEffect(() => {
    if (open) {
      loadData();
    }
  }, [open]);

  const loadData = async () => {
    try {
      // Загружаем картриджи и объекты параллельно
      const [cartridgesResponse, activeLocations] = await Promise.all([
        getCartridges(0, 100), // Получаем первые 100 картриджей
        getActiveLocations()
      ]);
      
      setCartridges(cartridgesResponse.content);
      setLocations(activeLocations);
    } catch (err) {
      console.error('Error loading data:', err);
      setError('Ошибка загрузки данных');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!cartridgeId) {
      setError('Выберите картридж');
      return;
    }

    if (!count || parseInt(count) <= 0) {
      setError('Количество должно быть больше 0');
      return;
    }

    try {
      setLoading(true);
      setError(null);

      const request: CreateOperationRequest = {
        type,
        count: parseInt(count),
        cartridgeId,
        locationId: locationId || undefined,
        notes: notes.trim() || undefined,
      };

      await createOperation(request);
      
      // Сброс формы
      setType('RECEIPT');
      setCartridgeId('');
      setLocationId('');
      setCount('1');
      setNotes('');
      
      onSuccess?.();
      onClose();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Ошибка создания операции');
      console.error('Error creating operation:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    if (!loading) {
      setType('RECEIPT');
      setCartridgeId('');
      setLocationId('');
      setCount('1');
      setNotes('');
      setError(null);
      onClose();
    }
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <DialogTitle>Добавить операцию</DialogTitle>
      <form onSubmit={handleSubmit}>
        <DialogContent>
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}
          
          <TextField
            margin="normal"
            required
            select
            fullWidth
            label="Тип операции"
            value={type}
            onChange={e => setType(e.target.value)}
            autoFocus
            disabled={loading}
          >
            {operationTypes.map(option => (
              <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
            ))}
          </TextField>
          
          <TextField
            margin="normal"
            required
            select
            fullWidth
            label="Картридж"
            value={cartridgeId}
            onChange={e => setCartridgeId(e.target.value)}
            disabled={loading}
          >
            <MenuItem value="">
              <em>Выберите картридж</em>
            </MenuItem>
            {cartridges.map(cartridge => (
              <MenuItem key={cartridge.id} value={cartridge.id}>
                {cartridge.model} {cartridge.serialNumber ? `(${cartridge.serialNumber})` : ''}
              </MenuItem>
            ))}
          </TextField>
          
          <TextField
            margin="normal"
            select
            fullWidth
            label="Объект"
            value={locationId}
            onChange={e => setLocationId(e.target.value)}
            disabled={loading}
          >
            <MenuItem value="">
              <em>Не выбрано</em>
            </MenuItem>
            {locations.map(location => (
              <MenuItem key={location.id} value={location.id}>
                {location.name}
              </MenuItem>
            ))}
          </TextField>
          
          <TextField
            margin="normal"
            required
            fullWidth
            label="Количество"
            type="number"
            value={count}
            onChange={e => setCount(e.target.value)}
            disabled={loading}
            inputProps={{ min: 1 }}
          />
          
          <TextField
            margin="normal"
            fullWidth
            label="Примечания"
            multiline
            rows={3}
            value={notes}
            onChange={e => setNotes(e.target.value)}
            disabled={loading}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} disabled={loading}>
            Отмена
          </Button>
          <Button 
            type="submit" 
            variant="contained" 
            disabled={loading}
            startIcon={loading ? <CircularProgress size={20} /> : null}
          >
            {loading ? 'Сохранение...' : 'Сохранить'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};

export default OperationFormDialog; 