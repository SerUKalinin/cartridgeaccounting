import React, { useState, useEffect } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, FormControlLabel, Checkbox, CircularProgress, Alert } from '@mui/material';
import { createLocation, updateLocation, CreateLocationRequest, LocationDto } from '../../api/locations';

interface LocationFormDialogProps {
  open: boolean;
  onClose: () => void;
  onSuccess?: () => void;
  location?: LocationDto | null;
}

const LocationFormDialog: React.FC<LocationFormDialogProps> = ({ open, onClose, onSuccess, location = null }) => {
  const [name, setName] = useState('');
  const [address, setAddress] = useState('');
  const [cabinet, setCabinet] = useState('');
  const [contactPerson, setContactPerson] = useState('');
  const [contactPhone, setContactPhone] = useState('');
  const [description, setDescription] = useState('');
  const [active, setActive] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const isEditMode = !!location;

  // Заполняем форму при редактировании
  useEffect(() => {
    if (open) {
      if (location) {
        setName(location.name);
        setAddress(location.address);
        setCabinet(location.cabinet || '');
        setContactPerson(location.contactPerson || '');
        setContactPhone(location.contactPhone || '');
        setDescription(location.description || '');
        setActive(location.active);
      } else {
        setName('');
        setAddress('');
        setCabinet('');
        setContactPerson('');
        setContactPhone('');
        setDescription('');
        setActive(true);
      }
      setError(null);
    }
  }, [open, location]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!name.trim()) {
      setError('Название объекта обязательно');
      return;
    }

    if (!address.trim()) {
      setError('Адрес объекта обязателен');
      return;
    }

    try {
      setLoading(true);
      setError(null);

      const request: CreateLocationRequest = {
        name: name.trim(),
        address: address.trim(),
        cabinet: cabinet.trim() || undefined,
        contactPerson: contactPerson.trim() || undefined,
        contactPhone: contactPhone.trim() || undefined,
        description: description.trim() || undefined,
        active,
      };

      if (isEditMode && location) {
        await updateLocation(location.id, request);
      } else {
        await createLocation(request);
      }
      
      onSuccess?.();
      onClose();
    } catch (err: any) {
      setError(err.response?.data?.message || `Ошибка ${isEditMode ? 'обновления' : 'создания'} объекта`);
      console.error(`Error ${isEditMode ? 'updating' : 'creating'} location:`, err);
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    if (!loading) {
      setName('');
      setAddress('');
      setCabinet('');
      setContactPerson('');
      setContactPhone('');
      setDescription('');
      setActive(true);
      setError(null);
      onClose();
    }
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <DialogTitle>{isEditMode ? 'Редактировать объект' : 'Добавить объект'}</DialogTitle>
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
            label="Название"
            value={name}
            onChange={e => setName(e.target.value)}
            autoFocus={!isEditMode}
            disabled={loading}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            label="Адрес"
            value={address}
            onChange={e => setAddress(e.target.value)}
            disabled={loading}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Кабинет"
            value={cabinet}
            onChange={e => setCabinet(e.target.value)}
            disabled={loading}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Контактное лицо"
            value={contactPerson}
            onChange={e => setContactPerson(e.target.value)}
            disabled={loading}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Телефон"
            value={contactPhone}
            onChange={e => setContactPhone(e.target.value)}
            disabled={loading}
          />
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
          <FormControlLabel
            control={
              <Checkbox 
                checked={active} 
                onChange={e => setActive(e.target.checked)} 
                color="primary"
                disabled={loading}
              />
            }
            label="Активен"
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
            {loading ? 'Сохранение...' : (isEditMode ? 'Обновить' : 'Сохранить')}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};

export default LocationFormDialog; 