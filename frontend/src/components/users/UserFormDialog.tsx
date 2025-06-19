import React, { useState, useEffect } from 'react';
import { 
  Dialog, 
  DialogTitle, 
  DialogContent, 
  DialogActions, 
  Button, 
  TextField, 
  MenuItem, 
  FormControlLabel, 
  Checkbox,
  CircularProgress,
  Alert
} from '@mui/material';
import { createUser, updateUser, UserDto, CreateUserRequest, UpdateUserRequest } from '../../api/users';
import { getRoleOptions } from '../../utils/roleTranslations';

interface UserFormDialogProps {
  open: boolean;
  onClose: () => void;
  user?: UserDto | null; // Для редактирования
  onSuccess?: () => void; // Callback при успешной операции
}

const UserFormDialog: React.FC<UserFormDialogProps> = ({ 
  open, 
  onClose, 
  user = null, 
  onSuccess 
}) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [role, setRole] = useState('OBJECT_USER');
  const [enabled, setEnabled] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const isEditMode = !!user;
  const roles = getRoleOptions();

  // Заполняем форму данными пользователя при редактировании
  useEffect(() => {
    if (user) {
      setUsername(user.username);
      setPassword(''); // Пароль не показываем при редактировании
      setFullName(user.fullName);
      setRole(user.role);
      setEnabled(user.enabled);
    } else {
      // Сброс формы при создании
      setUsername('');
      setPassword('');
      setFullName('');
      setRole('OBJECT_USER');
      setEnabled(true);
    }
    setError(null);
  }, [user, open]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!isEditMode && !password.trim()) {
      setError('Пароль обязателен при создании пользователя');
      return;
    }

    try {
      setLoading(true);
      setError(null);

      if (isEditMode && user) {
        // Редактирование пользователя
        const updateData: UpdateUserRequest = {
          fullName,
          role,
          enabled
        };
        
        // Добавляем пароль только если он введен
        if (password.trim()) {
          updateData.password = password;
        }
        
        await updateUser(user.id, updateData);
      } else {
        // Создание нового пользователя
        const createData: CreateUserRequest = {
          username,
          password,
          fullName,
          role
        };
        await createUser(createData);
      }

      onSuccess?.();
      onClose();
    } catch (err: any) {
      console.error('Error saving user:', err);
      setError(err.response?.data?.message || 'Ошибка при сохранении пользователя');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    if (!loading) {
      onClose();
    }
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <DialogTitle>
        {isEditMode ? 'Редактировать пользователя' : 'Добавить пользователя'}
      </DialogTitle>
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
            label="Логин"
            value={username}
            onChange={e => setUsername(e.target.value)}
            disabled={isEditMode} // Логин нельзя изменять при редактировании
            autoFocus={!isEditMode}
          />
          
          <TextField
            margin="normal"
            required={!isEditMode}
            fullWidth
            label={isEditMode ? "Новый пароль (оставьте пустым, чтобы не изменять)" : "Пароль"}
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            autoFocus={isEditMode}
          />
          
          <TextField
            margin="normal"
            required
            fullWidth
            label="ФИО"
            value={fullName}
            onChange={e => setFullName(e.target.value)}
          />
          
          <TextField
            margin="normal"
            required
            select
            fullWidth
            label="Роль"
            value={role}
            onChange={e => setRole(e.target.value)}
          >
            {roles.map(option => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
          
          <FormControlLabel
            control={
              <Checkbox 
                checked={enabled} 
                onChange={e => setEnabled(e.target.checked)} 
                color="primary" 
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
            startIcon={loading ? <CircularProgress size={16} /> : undefined}
          >
            {loading ? 'Сохранение...' : 'Сохранить'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};

export default UserFormDialog; 