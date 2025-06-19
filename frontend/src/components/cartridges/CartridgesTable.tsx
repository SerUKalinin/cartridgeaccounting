import React, { useState, useEffect } from 'react';
import { 
  Box, 
  TextField, 
  MenuItem, 
  Table, 
  TableBody, 
  TableCell, 
  TableContainer, 
  TableHead, 
  TableRow, 
  Paper, 
  CircularProgress, 
  Alert, 
  Pagination,
  IconButton,
  Tooltip
} from '@mui/material';
import { Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import { getCartridges, CartridgeDto } from '../../api/cartridges';

const statuses = [
  { value: '', label: 'Все статусы' },
  { value: 'IN_STOCK', label: 'На складе' },
  { value: 'IN_USE', label: 'В использовании' },
  { value: 'REFILLING', label: 'На заправке' },
  { value: 'DISPOSED', label: 'Списан' },
];

interface CartridgesTableProps {
  onEditCartridge?: (cartridge: CartridgeDto) => void;
  onDeleteCartridge?: (cartridge: CartridgeDto) => void;
  initialStatus?: string;
}

const CartridgesTable: React.FC<CartridgesTableProps> = ({ onEditCartridge, onDeleteCartridge, initialStatus }) => {
  const [cartridges, setCartridges] = useState<CartridgeDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filterModel, setFilterModel] = useState('');
  const [filterStatus, setFilterStatus] = useState(initialStatus || '');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const loadCartridges = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await getCartridges(page, 10);
      setCartridges(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (err) {
      setError('Ошибка загрузки картриджей');
      console.error('Error loading cartridges:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCartridges();
  }, [page]);

  useEffect(() => {
    if (initialStatus && initialStatus !== filterStatus) {
      setFilterStatus(initialStatus);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [initialStatus]);

  const filteredCartridges = cartridges.filter(cartridge =>
    cartridge.model.toLowerCase().includes(filterModel.toLowerCase()) &&
    (filterStatus ? cartridge.status === filterStatus : true)
  );

  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value - 1); // API использует 0-based индексацию
  };

  const handleEditCartridge = (cartridge: CartridgeDto) => {
    if (onEditCartridge) {
      onEditCartridge(cartridge);
    }
  };

  const handleDeleteCartridge = (cartridge: CartridgeDto) => {
    if (onDeleteCartridge) {
      onDeleteCartridge(cartridge);
    }
  };

  const getStatusLabel = (status: string) => {
    const statusMap: { [key: string]: string } = {
      'IN_STOCK': 'На складе',
      'IN_USE': 'В использовании',
      'REFILLING': 'На заправке',
      'DISPOSED': 'Списан'
    };
    return statusMap[status] || status;
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" p={3}>
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

  return (
    <Box>
      <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
        <TextField
          label="Поиск по модели"
          value={filterModel}
          onChange={e => setFilterModel(e.target.value)}
        />
        <TextField
          select
          label="Статус"
          value={filterStatus}
          onChange={e => setFilterStatus(e.target.value)}
          sx={{ minWidth: 180 }}
        >
          {statuses.map(option => (
            <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
          ))}
        </TextField>
      </Box>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Модель</TableCell>
              <TableCell>Бренд</TableCell>
              <TableCell>Артикул</TableCell>
              <TableCell>Цвет</TableCell>
              <TableCell>Совместимость</TableCell>
              <TableCell>Серийный номер</TableCell>
              <TableCell>Статус</TableCell>
              <TableCell>Ресурс (стр.)</TableCell>
              <TableCell>Местоположение</TableCell>
              <TableCell>Дата создания</TableCell>
              <TableCell align="center">Действия</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredCartridges.map((cartridge) => (
              <TableRow key={cartridge.id}>
                <TableCell>{cartridge.model}</TableCell>
                <TableCell>{cartridge.brand || '-'}</TableCell>
                <TableCell>{cartridge.partNumber || '-'}</TableCell>
                <TableCell>{cartridge.color || '-'}</TableCell>
                <TableCell>{cartridge.compatiblePrinters || '-'}</TableCell>
                <TableCell>{cartridge.serialNumber || '-'}</TableCell>
                <TableCell>{getStatusLabel(cartridge.status)}</TableCell>
                <TableCell>{cartridge.resourcePages || '-'}</TableCell>
                <TableCell>{cartridge.currentLocationName || '-'}</TableCell>
                <TableCell>{new Date(cartridge.createdAt).toLocaleDateString('ru-RU')}</TableCell>
                <TableCell align="center">
                  <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                    <Tooltip title="Редактировать">
                      <IconButton 
                        color="primary" 
                        onClick={() => handleEditCartridge(cartridge)}
                        size="small"
                      >
                        <EditIcon />
                      </IconButton>
                    </Tooltip>
                    <Tooltip title="Удалить">
                      <IconButton 
                        color="error" 
                        onClick={() => handleDeleteCartridge(cartridge)}
                        size="small"
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Tooltip>
                  </Box>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {totalPages > 1 && (
        <Box display="flex" justifyContent="center" mt={2}>
          <Pagination 
            count={totalPages} 
            page={page + 1} 
            onChange={handlePageChange}
            color="primary"
          />
        </Box>
      )}
    </Box>
  );
};

export default CartridgesTable; 