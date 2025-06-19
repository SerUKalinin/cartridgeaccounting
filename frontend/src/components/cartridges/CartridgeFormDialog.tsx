import React, { useState, useEffect } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, MenuItem, CircularProgress, Alert } from '@mui/material';
import { createCartridge, updateCartridge, CreateCartridgeRequest, CartridgeDto } from '../../api/cartridges';
import { getActiveLocations, LocationDto } from '../../api/locations';

const statuses = [
  { value: 'IN_STOCK', label: 'На складе' },
  { value: 'IN_USE', label: 'В использовании' },
  { value: 'REFILLING', label: 'На заправке' },
  { value: 'DISPOSED', label: 'Списан' },
];

interface CartridgeFormDialogProps {
  open: boolean;
  onClose: () => void;
  onSuccess?: () => void;
  cartridge?: CartridgeDto | null;
}

const CartridgeFormDialog: React.FC<CartridgeFormDialogProps> = ({ open, onClose, onSuccess, cartridge = null }) => {
  const [model, setModel] = useState('');
  const [serialNumber, setSerialNumber] = useState('');
  const [status, setStatus] = useState('IN_STOCK');
  const [resourcePages, setResourcePages] = useState('');
  const [description, setDescription] = useState('');
  const [locationId, setLocationId] = useState('');
  const [locations, setLocations] = useState<LocationDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [brand, setBrand] = useState('');
  const [partNumber, setPartNumber] = useState('');
  const [color, setColor] = useState('');
  const [compatiblePrinters, setCompatiblePrinters] = useState('');

  const isEditMode = !!cartridge;

  // Заполняем форму при редактировании
  useEffect(() => {
    if (open) {
      loadLocations();
      if (cartridge) {
        setModel(cartridge.model);
        setSerialNumber(cartridge.serialNumber || '');
        setStatus(cartridge.status);
        setResourcePages(cartridge.resourcePages ? String(cartridge.resourcePages) : '');
        setDescription(cartridge.description || '');
        setLocationId(cartridge.currentLocationId || '');
        setBrand(cartridge.brand || '');
        setPartNumber(cartridge.partNumber || '');
        setColor(cartridge.color || '');
        setCompatiblePrinters(cartridge.compatiblePrinters || '');
      } else {
        setModel('');
        setSerialNumber('');
        setStatus('IN_STOCK');
        setResourcePages('');
        setDescription('');
        setLocationId('');
        setBrand('');
        setPartNumber('');
        setColor('');
        setCompatiblePrinters('');
      }
      setError(null);
    }
    // eslint-disable-next-line
  }, [open, cartridge]);

  const loadLocations = async () => {
    try {
      const activeLocations = await getActiveLocations();
      setLocations(activeLocations);
    } catch (err) {
      console.error('Error loading locations:', err);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!model.trim()) {
      setError('Модель картриджа обязательна');
      return;
    }
    try {
      setLoading(true);
      setError(null);
      const request: CreateCartridgeRequest = {
        model: model.trim(),
        serialNumber: serialNumber.trim() || undefined,
        status,
        resourcePages: resourcePages ? parseInt(resourcePages) : undefined,
        description: description.trim() || undefined,
        currentLocationId: locationId || undefined,
        brand: brand.trim() || undefined,
        partNumber: partNumber.trim() || undefined,
        color: color.trim() || undefined,
        compatiblePrinters: compatiblePrinters.trim() || undefined,
      };
      
      console.log('Отправляем запрос:', request);
      console.log('Режим редактирования:', isEditMode);
      
      if (isEditMode && cartridge) {
        console.log('Обновляем картридж с ID:', cartridge.id);
        const result = await updateCartridge(cartridge.id, request);
        console.log('Результат обновления:', result);
      } else {
        console.log('Создаем новый картридж');
        const result = await createCartridge(request);
        console.log('Результат создания:', result);
      }
      onSuccess?.();
      onClose();
    } catch (err: any) {
      console.error('Ошибка при сохранении картриджа:', err);
      setError(err.response?.data?.message || 'Ошибка сохранения картриджа');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    if (!loading) {
      setModel('');
      setSerialNumber('');
      setStatus('IN_STOCK');
      setResourcePages('');
      setDescription('');
      setLocationId('');
      setBrand('');
      setPartNumber('');
      setColor('');
      setCompatiblePrinters('');
      setError(null);
      onClose();
    }
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <DialogTitle>{isEditMode ? 'Редактировать картридж' : 'Добавить картридж'}</DialogTitle>
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
            fullWidth
            label="Модель"
            value={model}
            onChange={e => setModel(e.target.value)}
            autoFocus={!isEditMode}
            disabled={loading || isEditMode}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Серийный номер"
            value={serialNumber}
            onChange={e => setSerialNumber(e.target.value)}
            disabled={loading || isEditMode}
          />
          <TextField
            margin="normal"
            required
            select
            fullWidth
            label="Статус"
            value={status}
            onChange={e => setStatus(e.target.value)}
            disabled={loading}
          >
            {statuses.map(option => (
              <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
            ))}
          </TextField>
          <TextField
            margin="normal"
            fullWidth
            label="Ресурс (страниц)"
            type="number"
            value={resourcePages}
            onChange={e => setResourcePages(e.target.value)}
            disabled={loading}
          />
          <TextField
            margin="normal"
            select
            fullWidth
            label="Местоположение"
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
            fullWidth
            label="Описание"
            multiline
            rows={3}
            value={description}
            onChange={e => setDescription(e.target.value)}
            disabled={loading}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Бренд"
            value={brand}
            onChange={e => setBrand(e.target.value)}
            disabled={loading}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Артикул производителя"
            value={partNumber}
            onChange={e => setPartNumber(e.target.value)}
            disabled={loading}
          />
          <TextField
            margin="normal"
            select
            fullWidth
            label="Цвет"
            value={color}
            onChange={e => setColor(e.target.value)}
            disabled={loading}
            SelectProps={{
              native: false,
            }}
          >
            <MenuItem value=""><em>Не выбран</em></MenuItem>
            <MenuItem value="Black">Black</MenuItem>
            <MenuItem value="Cyan">Cyan</MenuItem>
            <MenuItem value="Magenta">Magenta</MenuItem>
            <MenuItem value="Yellow">Yellow</MenuItem>
            <MenuItem value="Drum">Drum</MenuItem>
            <MenuItem value="Other">Other</MenuItem>
          </TextField>
          <TextField
            margin="normal"
            fullWidth
            label="Совместимость с принтерами"
            value={compatiblePrinters}
            onChange={e => setCompatiblePrinters(e.target.value)}
            disabled={loading}
            multiline
            rows={2}
            placeholder="Например: MFP M477, MFP M479 и т.д."
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

export default CartridgeFormDialog; 