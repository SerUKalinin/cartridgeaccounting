import React, { useState } from 'react';
import {
  Box,
  Typography,
  Card,
  CardContent,
  Button,
  TextField,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  Alert,
  Snackbar,
  CircularProgress,
  Divider
} from '@mui/material';
import Grid from '@mui/material/Grid';
import {
  FileDownload as FileDownloadIcon,
  Assessment as AssessmentIcon,
  Inventory as InventoryIcon,
  History as HistoryIcon,
  People as PeopleIcon,
  LocationOn as LocationIcon,
  DataObject as DataObjectIcon
} from '@mui/icons-material';
import {
  exportCartridges,
  exportOperations,
  exportUsers,
  exportLocations,
  exportAll
} from '../api/export';

const ReportsPage: React.FC = () => {
  const [loading, setLoading] = useState<string | null>(null);
  const [snackbar, setSnackbar] = useState<{
    open: boolean;
    message: string;
    severity: 'success' | 'error';
  }>({ open: false, message: '', severity: 'success' });

  // Фильтры для картриджей
  const [cartridgeStatus, setCartridgeStatus] = useState('');
  const [cartridgeModel, setCartridgeModel] = useState('');

  // Фильтры для операций
  const [operationType, setOperationType] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

  const handleExport = async (exportFunction: () => Promise<boolean>, type: string) => {
    try {
      setLoading(type);
      await exportFunction();
      setSnackbar({
        open: true,
        message: `Отчёт ${type} успешно экспортирован`,
        severity: 'success'
      });
    } catch (error) {
      console.error(`Error exporting ${type}:`, error);
      setSnackbar({
        open: true,
        message: `Ошибка при экспорте ${type}`,
        severity: 'error'
      });
    } finally {
      setLoading(null);
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  const cartridgeStatuses = [
    { value: '', label: 'Все статусы' },
    { value: 'IN_STOCK', label: 'На складе' },
    { value: 'IN_USE', label: 'В использовании' },
    { value: 'REFILLING', label: 'На заправке' },
    { value: 'DISPOSED', label: 'Списан' },
  ];

  const operationTypes = [
    { value: '', label: 'Все типы' },
    { value: 'RECEIPT', label: 'Поступление' },
    { value: 'ISSUE', label: 'Выдача' },
    { value: 'RETURN', label: 'Возврат' },
    { value: 'REFILL', label: 'Заправка' },
    { value: 'DISPOSAL', label: 'Списание' },
    { value: 'TRANSFER', label: 'Перемещение' },
  ];

  return (
    <Box>
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
        <AssessmentIcon sx={{ fontSize: 32, mr: 2, color: 'primary.main' }} />
        <Typography variant="h4">Отчёты</Typography>
      </Box>

      <Typography variant="body1" mb={4} color="text.secondary">
        Экспортируйте данные системы в Excel файлы для анализа и отчётности
      </Typography>

      <Grid container spacing={3}>
        {/* Экспорт картриджей */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <InventoryIcon sx={{ fontSize: 24, mr: 1, color: 'secondary.main' }} />
                <Typography variant="h6">Картриджи</Typography>
              </Box>
              <Typography variant="body2" color="text.secondary" mb={3}>
                Экспорт всех картриджей с возможностью фильтрации по статусу и модели
              </Typography>
              
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mb: 3 }}>
                <FormControl fullWidth size="small">
                  <InputLabel>Статус</InputLabel>
                  <Select
                    value={cartridgeStatus}
                    onChange={(e) => setCartridgeStatus(e.target.value)}
                    label="Статус"
                  >
                    {cartridgeStatuses.map(option => (
                      <MenuItem key={option.value} value={option.value}>
                        {option.label}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                
                <TextField
                  label="Модель"
                  value={cartridgeModel}
                  onChange={(e) => setCartridgeModel(e.target.value)}
                  size="small"
                  placeholder="Фильтр по модели"
                />
              </Box>

              <Button
                variant="contained"
                startIcon={loading === 'cartridges' ? <CircularProgress size={20} /> : <FileDownloadIcon />}
                onClick={() => handleExport(
                  () => exportCartridges(cartridgeStatus || undefined, cartridgeModel || undefined),
                  'картриджей'
                )}
                disabled={loading !== null}
                fullWidth
                color="secondary"
              >
                {loading === 'cartridges' ? 'Экспорт...' : 'Экспорт картриджей'}
              </Button>
            </CardContent>
          </Card>
        </Grid>

        {/* Экспорт операций */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <HistoryIcon sx={{ fontSize: 24, mr: 1, color: 'info.main' }} />
                <Typography variant="h6">Операции</Typography>
              </Box>
              <Typography variant="body2" color="text.secondary" mb={3}>
                Экспорт истории операций с фильтрацией по типу и датам
              </Typography>
              
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mb: 3 }}>
                <FormControl fullWidth size="small">
                  <InputLabel>Тип операции</InputLabel>
                  <Select
                    value={operationType}
                    onChange={(e) => setOperationType(e.target.value)}
                    label="Тип операции"
                  >
                    {operationTypes.map(option => (
                      <MenuItem key={option.value} value={option.value}>
                        {option.label}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                
                <TextField
                  label="Дата начала"
                  type="date"
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                  size="small"
                  InputLabelProps={{ shrink: true }}
                />
                
                <TextField
                  label="Дата окончания"
                  type="date"
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                  size="small"
                  InputLabelProps={{ shrink: true }}
                />
              </Box>

              <Button
                variant="contained"
                startIcon={loading === 'operations' ? <CircularProgress size={20} /> : <FileDownloadIcon />}
                onClick={() => handleExport(
                  () => exportOperations(operationType || undefined, startDate || undefined, endDate || undefined),
                  'операций'
                )}
                disabled={loading !== null}
                fullWidth
                color="info"
              >
                {loading === 'operations' ? 'Экспорт...' : 'Экспорт операций'}
              </Button>
            </CardContent>
          </Card>
        </Grid>

        {/* Экспорт пользователей */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <PeopleIcon sx={{ fontSize: 24, mr: 1, color: 'success.main' }} />
                <Typography variant="h6">Пользователи</Typography>
              </Box>
              <Typography variant="body2" color="text.secondary" mb={3}>
                Экспорт списка всех пользователей системы
              </Typography>

              <Button
                variant="contained"
                startIcon={loading === 'users' ? <CircularProgress size={20} /> : <FileDownloadIcon />}
                onClick={() => handleExport(exportUsers, 'пользователей')}
                disabled={loading !== null}
                fullWidth
                color="success"
              >
                {loading === 'users' ? 'Экспорт...' : 'Экспорт пользователей'}
              </Button>
            </CardContent>
          </Card>
        </Grid>

        {/* Экспорт объектов */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <LocationIcon sx={{ fontSize: 24, mr: 1, color: 'warning.main' }} />
                <Typography variant="h6">Объекты</Typography>
              </Box>
              <Typography variant="body2" color="text.secondary" mb={3}>
                Экспорт списка всех объектов с количеством картриджей
              </Typography>

              <Button
                variant="contained"
                startIcon={loading === 'locations' ? <CircularProgress size={20} /> : <FileDownloadIcon />}
                onClick={() => handleExport(exportLocations, 'объектов')}
                disabled={loading !== null}
                fullWidth
                color="warning"
              >
                {loading === 'locations' ? 'Экспорт...' : 'Экспорт объектов'}
              </Button>
            </CardContent>
          </Card>
        </Grid>

        {/* Экспорт всех данных */}
        <Grid item xs={12}>
          <Divider sx={{ my: 2 }} />
          <Card sx={{ bgcolor: 'grey.50' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <DataObjectIcon sx={{ fontSize: 24, mr: 1, color: 'primary.main' }} />
                <Typography variant="h6">Полный отчёт</Typography>
              </Box>
              <Typography variant="body2" color="text.secondary" mb={3}>
                Экспорт всех данных системы в один Excel файл с отдельными листами для каждого типа данных
              </Typography>

              <Button
                variant="contained"
                size="large"
                startIcon={loading === 'all' ? <CircularProgress size={20} /> : <FileDownloadIcon />}
                onClick={() => handleExport(exportAll, 'всех данных')}
                disabled={loading !== null}
                fullWidth
                sx={{ py: 1.5 }}
              >
                {loading === 'all' ? 'Экспорт...' : 'Экспорт всех данных'}
              </Button>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Уведомления */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
      >
        <Alert 
          onClose={handleCloseSnackbar} 
          severity={snackbar.severity}
          sx={{ width: '100%' }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default ReportsPage; 