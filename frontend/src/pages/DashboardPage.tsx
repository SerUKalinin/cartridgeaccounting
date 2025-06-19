import React, { useState, useEffect } from 'react';
import { Box, Typography, Card, CardContent, Button, CircularProgress, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { getUsers } from '../api/users';
import { getCartridges, getCartridgeCountByStatus } from '../api/cartridges';
import { getLocations } from '../api/locations';
import { getOperations } from '../api/operations';

interface DashboardStats {
  users: number;
  cartridges: number;
  locations: number;
  operations: number;
  cartridgesInStock: number;
  cartridgesInUse: number;
  cartridgesRefilling: number;
  cartridgesDisposed: number;
}

const DashboardPage: React.FC = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const loadDashboardStats = async () => {
    try {
      setLoading(true);
      setError(null);

      // Загружаем все данные параллельно
      const [
        usersResponse,
        cartridgesResponse,
        locationsResponse,
        operationsResponse,
        inStockCount,
        inUseCount,
        refillingCount,
        disposedCount
      ] = await Promise.all([
        getUsers(0, 1), // Получаем только количество
        getCartridges(0, 1), // Получаем только количество
        getLocations(0, 1), // Получаем только количество
        getOperations(0, 1), // Получаем только количество
        getCartridgeCountByStatus('IN_STOCK'),
        getCartridgeCountByStatus('IN_USE'),
        getCartridgeCountByStatus('REFILLING'),
        getCartridgeCountByStatus('DISPOSED')
      ]);

      setStats({
        users: usersResponse.totalElements,
        cartridges: cartridgesResponse.totalElements,
        locations: locationsResponse.totalElements,
        operations: operationsResponse.totalElements,
        cartridgesInStock: inStockCount,
        cartridgesInUse: inUseCount,
        cartridgesRefilling: refillingCount,
        cartridgesDisposed: disposedCount
      });
    } catch (err) {
      setError('Ошибка загрузки статистики');
      console.error('Error loading dashboard stats:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDashboardStats();
  }, []);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mb: 2 }}>
        {error}
      </Alert>
    );
  }

  if (!stats) {
    return null;
  }

  const mainStats = [
    { label: 'Пользователи', value: stats.users, link: '/users', color: 'primary' },
    { label: 'Картриджи', value: stats.cartridges, link: '/cartridges', color: 'secondary' },
    { label: 'Объекты', value: stats.locations, link: '/locations', color: 'success' },
    { label: 'Операции', value: stats.operations, link: '/operations', color: 'info' },
  ];

  const cartridgeStats = [
    { label: 'На складе', value: stats.cartridgesInStock, color: 'success.main', status: 'IN_STOCK' },
    { label: 'В использовании', value: stats.cartridgesInUse, color: 'warning.main', status: 'IN_USE' },
    { label: 'На заправке', value: stats.cartridgesRefilling, color: 'info.main', status: 'REFILLING' },
    { label: 'Списано', value: stats.cartridgesDisposed, color: 'error.main', status: 'DISPOSED' },
  ];

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Добро пожаловать в систему учёта картриджей МФУ!
      </Typography>
      <Typography variant="body1" mb={4}>
        Выберите раздел для работы или воспользуйтесь быстрыми действиями ниже.
      </Typography>

      {/* Основная статистика */}
      <Typography variant="h5" gutterBottom sx={{ mt: 4, mb: 2 }}>
        Общая статистика
      </Typography>
      <Box sx={{ 
        display: 'grid', 
        gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', 
        gap: 3, 
        mb: 4 
      }}>
        {mainStats.map(stat => (
          <Card key={stat.label}>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                {stat.label}
              </Typography>
              <Typography variant="h4" color={stat.color} gutterBottom>
                {stat.value}
              </Typography>
              <Button 
                variant="outlined" 
                onClick={() => navigate(stat.link)} 
                fullWidth
                color={stat.color as any}
              >
                Перейти
              </Button>
            </CardContent>
          </Card>
        ))}
      </Box>

      {/* Статистика картриджей по статусам */}
      <Typography variant="h5" gutterBottom sx={{ mt: 4, mb: 2 }}>
        Статистика картриджей
      </Typography>
      <Box sx={{ 
        display: 'grid', 
        gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', 
        gap: 3 
      }}>
        {cartridgeStats.map(stat => (
          <Card key={stat.label}>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                {stat.label}
              </Typography>
              <Typography variant="h4" sx={{ color: stat.color }} gutterBottom>
                {stat.value}
              </Typography>
              <Button 
                variant="outlined" 
                onClick={() => navigate(`/cartridges?status=${stat.status}`)} 
                fullWidth
                sx={{ color: stat.color, borderColor: stat.color }}
              >
                Просмотреть
              </Button>
            </CardContent>
          </Card>
        ))}
      </Box>
    </Box>
  );
};

export default DashboardPage; 