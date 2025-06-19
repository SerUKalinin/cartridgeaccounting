import React, { useState, useEffect } from 'react';
import { 
  Box, 
  TextField, 
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
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { getLocations, LocationDto } from '../../api/locations';

interface LocationsTableProps {
  onEditLocation?: (location: LocationDto) => void;
  onDeleteLocation?: (location: LocationDto) => void;
}

const LocationsTable: React.FC<LocationsTableProps> = ({ onEditLocation, onDeleteLocation }) => {
  const [locations, setLocations] = useState<LocationDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filterName, setFilterName] = useState('');
  const [filterAddress, setFilterAddress] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const loadLocations = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await getLocations(page, 10);
      setLocations(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (err) {
      setError('Ошибка загрузки объектов');
      console.error('Error loading locations:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadLocations();
  }, [page]);

  const filteredLocations = locations.filter(location =>
    location.name.toLowerCase().includes(filterName.toLowerCase()) &&
    location.address.toLowerCase().includes(filterAddress.toLowerCase())
  );

  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value - 1); // API использует 0-based индексацию
  };

  const handleEdit = (location: LocationDto) => {
    onEditLocation?.(location);
  };

  const handleDelete = (location: LocationDto) => {
    onDeleteLocation?.(location);
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
          label="Поиск по названию"
          value={filterName}
          onChange={e => setFilterName(e.target.value)}
        />
        <TextField
          label="Поиск по адресу"
          value={filterAddress}
          onChange={e => setFilterAddress(e.target.value)}
        />
      </Box>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Название</TableCell>
              <TableCell>Количество картриджей</TableCell>
              <TableCell>Адрес</TableCell>
              <TableCell>Кабинет</TableCell>
              <TableCell>Контактное лицо</TableCell>
              <TableCell>Телефон</TableCell>
              <TableCell>Активен</TableCell>
              <TableCell align="center">Действия</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredLocations.map((location) => (
              <TableRow key={location.id}>
                <TableCell>{location.name}</TableCell>
                <TableCell>{location.cartridgeCount}</TableCell>
                <TableCell>{location.address}</TableCell>
                <TableCell>{location.cabinet || '-'}</TableCell>
                <TableCell>{location.contactPerson || '-'}</TableCell>
                <TableCell>{location.contactPhone || '-'}</TableCell>
                <TableCell>{location.active ? 'Да' : 'Нет'}</TableCell>
                <TableCell align="center">
                  <Tooltip title="Редактировать">
                    <IconButton 
                      onClick={() => handleEdit(location)}
                      color="primary"
                      size="small"
                    >
                      <EditIcon />
                    </IconButton>
                  </Tooltip>
                  <Tooltip title="Удалить">
                    <IconButton 
                      onClick={() => handleDelete(location)}
                      color="error"
                      size="small"
                    >
                      <DeleteIcon />
                    </IconButton>
                  </Tooltip>
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

export default LocationsTable; 