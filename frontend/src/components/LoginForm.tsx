import React, { useState } from 'react';
import { Box, Button, TextField, Typography, Paper, Avatar, FormControlLabel, Checkbox } from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { login, AuthRequest } from '../api/auth';

interface LoginFormProps {
  onLoginSuccess: (token: string, role: string) => void;
}

const LoginForm: React.FC<LoginFormProps> = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [remember, setRemember] = useState(true);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const data: AuthRequest = { username, password };
      const res = await login(data);
      if (remember) {
        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
      } else {
        sessionStorage.setItem('token', res.token);
        sessionStorage.setItem('role', res.role);
      }
      onLoginSuccess(res.token, res.role);
    } catch (err: any) {
      setError(err.response?.data || 'Ошибка авторизации');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Paper elevation={6} sx={{ p: 4, maxWidth: 400, width: '100%', height: '100%', maxHeight: '100vh' }}>
      <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mb: 2 }}>
        <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Вход в систему
        </Typography>
      </Box>
      <Box component="form" onSubmit={handleSubmit}>
        <TextField
          margin="normal"
          required
          fullWidth
          label="Логин"
          value={username}
          onChange={(e: React.ChangeEvent<HTMLInputElement>) => setUsername(e.target.value)}
          autoFocus
        />
        <TextField
          margin="normal"
          required
          fullWidth
          label="Пароль"
          type="password"
          value={password}
          onChange={(e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
        />
        <FormControlLabel
          control={<Checkbox checked={remember} onChange={(e: React.ChangeEvent<HTMLInputElement>) => setRemember(e.target.checked)} color="primary" />}
          label="Остаться в сети"
        />
        {error && <Typography color="error" variant="body2">{error}</Typography>}
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}
          disabled={loading}
        >
          {loading ? 'Вход...' : 'Войти'}
        </Button>
      </Box>
    </Paper>
  );
};

export default LoginForm; 