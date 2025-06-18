import React, { useState } from 'react';
import { Box, Button, TextField, Typography, Avatar, MenuItem, FormControlLabel, Checkbox, Paper } from '@mui/material';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import axios from 'axios';

const roles = [
  { value: 'ADMIN', label: 'Администратор' },
  { value: 'WAREHOUSE_MANAGER', label: 'Заведующий складом' },
  { value: 'OBJECT_USER', label: 'Пользователь объекта' },
];

interface RegistrationFormProps {
  onSuccess: () => void;
}

const RegistrationForm: React.FC<RegistrationFormProps> = ({ onSuccess }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [role, setRole] = useState('OBJECT_USER');
  const [enabled, setEnabled] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);
    try {
      await axios.post('/api/users', {
        username,
        password,
        fullName,
        role,
        enabled,
      });
      setSuccess('Регистрация успешна! Теперь вы можете войти.');
      setTimeout(() => {
        setSuccess('');
        onSuccess();
      }, 1500);
    } catch (err: any) {
      setError(err.response?.data || 'Ошибка регистрации');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Paper elevation={6} sx={{ p: 4, maxWidth: 400, width: '100%', height: '100%', maxHeight: '100vh' }}>
      <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mb: 2 }}>
        <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
          <PersonAddIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Регистрация
        </Typography>
      </Box>
      <Box component="form" onSubmit={handleSubmit}>
        <TextField
          margin="normal"
          required
          fullWidth
          label="Логин"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          autoFocus
        />
        <TextField
          margin="normal"
          required
          fullWidth
          label="Пароль"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <TextField
          margin="normal"
          required
          fullWidth
          label="ФИО"
          value={fullName}
          onChange={(e) => setFullName(e.target.value)}
        />
        <TextField
          margin="normal"
          required
          select
          fullWidth
          label="Роль"
          value={role}
          onChange={(e) => setRole(e.target.value)}
        >
          {roles.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>
        <FormControlLabel
          control={<Checkbox checked={enabled} onChange={(e) => setEnabled(e.target.checked)} color="primary" />}
          label="Активен"
        />
        {error && <Typography color="error" variant="body2">{error}</Typography>}
        {success && <Typography color="primary" variant="body2">{success}</Typography>}
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}
          disabled={loading}
        >
          {loading ? 'Регистрация...' : 'Зарегистрироваться'}
        </Button>
      </Box>
    </Paper>
  );
};

export default RegistrationForm; 