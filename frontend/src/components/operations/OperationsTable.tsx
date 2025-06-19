import React, { useState, useEffect } from 'react';
import { Box, TextField, MenuItem, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, CircularProgress, Alert, Pagination } from '@mui/material';
import { getOperations, OperationDto } from '../../api/operations';

const operationTypes = [
  { value: '', label: 'Все типы' },
  { value: 'RECEIPT', label: 'Поступление' },
  { value: 'ISSUE', label: 'Выдача' },
  { value: 'RETURN', label: 'Возврат' },
  { value: 'REFILL', label: 'Заправка' },
  { value: 'DISPOSAL', label: 'Списание' },
];

const OperationsTable: React.FC = () => {
  const [operations, setOperations] = useState<OperationDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filterType, setFilterType] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const loadOperations = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await getOperations(page, 10);
      setOperations(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (err) {
      setError('Ошибка загрузки операций');
      console.error('Error loading operations:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOperations();
  }, [page]);

  const filteredOperations = operations.filter(operation =>
    filterType ? operation.type === filterType : true
  );

  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value - 1); // API использует 0-based индексацию
  };

  const getTypeLabel = (type: string) => {
    const typeMap: { [key: string]: string } = {
      'RECEIPT': 'Поступление',
      'ISSUE': 'Выдача',
      'RETURN': 'Возврат',
      'REFILL': 'Заправка',
      'DISPOSAL': 'Списание'
    };
    return typeMap[type] || type;
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString('ru-RU', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
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
          select
          label="Тип операции"
          value={filterType}
          onChange={e => setFilterType(e.target.value)}
          sx={{ minWidth: 180 }}
        >
          {operationTypes.map(option => (
            <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
          ))}
        </TextField>
      </Box>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Тип</TableCell>
              <TableCell>Картридж</TableCell>
              <TableCell>Серийный номер</TableCell>
              <TableCell>Объект</TableCell>
              <TableCell>Количество</TableCell>
              <TableCell>Дата</TableCell>
              <TableCell>Примечания</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredOperations.map((operation) => (
              <TableRow key={operation.id}>
                <TableCell>{getTypeLabel(operation.type)}</TableCell>
                <TableCell>{operation.cartridgeModel || '-'}</TableCell>
                <TableCell>{operation.cartridgeSerialNumber || '-'}</TableCell>
                <TableCell>{operation.locationName || '-'}</TableCell>
                <TableCell>{operation.count}</TableCell>
                <TableCell>{formatDate(operation.operationDate)}</TableCell>
                <TableCell>{operation.notes || '-'}</TableCell>
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

export default OperationsTable; 