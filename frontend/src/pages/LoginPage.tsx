import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import LoginForm from '../components/LoginForm';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();

  const handleLoginSuccess = (token: string, role: string) => {
    navigate('/dashboard');
  };

  return (
    <>
      <LoginForm onLoginSuccess={handleLoginSuccess} />
      <div style={{ textAlign: 'center', marginTop: 16 }}>
        <Link to="/register">Нет аккаунта? Зарегистрироваться</Link>
      </div>
    </>
  );
};

export default LoginPage; 