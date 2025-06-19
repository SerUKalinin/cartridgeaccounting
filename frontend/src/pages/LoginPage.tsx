import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import RegistrationForm from '../components/RegistrationForm';
import SwitchAuthModeButton from '../components/SwitchAuthModeButton';
import { Box, Typography } from '@mui/material';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const [showRegister, setShowRegister] = useState(false);
  const [registerSuccess, setRegisterSuccess] = useState(false);

  // Проверяем, есть ли уже токен при загрузке страницы
  useEffect(() => {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (token) {
      const redirectPath = localStorage.getItem('redirectPath') || '/dashboard';
      localStorage.removeItem('redirectPath');
      navigate(redirectPath, { replace: true });
    }
  }, [navigate]);

  const handleLoginSuccess = (token: string, role: string) => {
    // После успешного входа перенаправляем на сохраненную страницу или dashboard
    const redirectPath = localStorage.getItem('redirectPath') || '/dashboard';
    localStorage.removeItem('redirectPath');
    navigate(redirectPath, { replace: true });
  };

  const handleRegisterSuccess = () => {
    setShowRegister(false);
    setRegisterSuccess(true);
  };

  return (
    <Box sx={{ bgcolor: 'background.default', display: 'flex', justifyContent: 'center' }}>
      <Box sx={{ width: '100%', maxWidth: 400 }}>
        {showRegister ? (
          <>
            <RegistrationForm onSuccess={handleRegisterSuccess} />
            <SwitchAuthModeButton onClick={() => setShowRegister(false)}>
              Уже есть аккаунт? Войти
            </SwitchAuthModeButton>
          </>
        ) : (
          <>
            <LoginForm onLoginSuccess={handleLoginSuccess} />
            {registerSuccess && (
              <Typography color="primary" align="center" mt={2}>
                Регистрация успешна! Теперь вы можете войти.
              </Typography>
            )}
            <SwitchAuthModeButton onClick={() => setShowRegister(true)}>
              Нет аккаунта? Зарегистрироваться
            </SwitchAuthModeButton>
          </>
        )}
      </Box>
    </Box>
  );
};

export default LoginPage;
