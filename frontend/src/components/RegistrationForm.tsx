import React, { useState } from 'react';
import { Box, Button, TextField, Typography, Avatar, MenuItem, FormControlLabel, Checkbox, Paper, Alert, CircularProgress } from '@mui/material';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import axios from 'axios';
import { register } from '../api/auth';
import { getRoleOptions } from '../utils/roleTranslations';

interface RegistrationFormProps {
  onSuccess: () => void;
}

const RegistrationForm: React.FC<RegistrationFormProps> = ({ onSuccess }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [role, setRole] = useState('OBJECT_USER');
  const [enabled, setEnabled] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const roles = getRoleOptions();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (password !== confirmPassword) {
      setError('Пароли не совпадают');
      return;
    }

    if (password.length < 6) {
      setError('Пароль должен содержать минимум 6 символов');
      return;
    }

    try {
      setLoading(true);
      setError(null);
      
      await register({
        username,
        password,
        fullName,
        role
      });
      
      onSuccess();
    } catch (err: any) {
      console.error('Registration error:', err);
      setError(err.response?.data?.message || 'Ошибка при регистрации');
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
          onChange={(e) => setUsername(e.target.value)}
          autoFocus
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
          label="Подтвердите пароль"
          type="password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
        <FormControlLabel
          control={<Checkbox checked={enabled} onChange={(e) => setEnabled(e.target.checked)} color="primary" />}
          label="Активен"
        />
        {success && <Typography color="primary" variant="body2">{success}</Typography>}
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}
          disabled={loading}
          startIcon={loading ? <CircularProgress size={16} /> : undefined}
        >
          {loading ? 'Регистрация...' : 'Зарегистрироваться'}
        </Button>
      </Box>
    </Paper>
  );
};

export default RegistrationForm; 