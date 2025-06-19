import React, { useState, useEffect } from 'react';
import { Box, TextField, MenuItem, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, CircularProgress, Alert, Pagination, IconButton, Tooltip } from '@mui/material';
import { Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import { getUsers, UserDto } from '../../api/users';
import { getRoleOptions, translateRole } from '../../utils/roleTranslations';

interface UsersTableProps {
  onEditUser?: (user: UserDto) => void;
  onDeleteUser?: (user: UserDto) => void;
}

const UsersTable: React.FC<UsersTableProps> = ({ onEditUser, onDeleteUser }) => {
  const [users, setUsers] = useState<UserDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filterUsername, setFilterUsername] = useState('');
  const [filterRole, setFilterRole] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const roles = [
    { value: '', label: 'Все роли' },
    ...getRoleOptions()
  ];

  const loadUsers = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await getUsers(page, 10);
      setUsers(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (err) {
      setError('Ошибка загрузки пользователей');
      console.error('Error loading users:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, [page]);

  const filteredUsers = users.filter(user =>
    user.username.toLowerCase().includes(filterUsername.toLowerCase()) &&
    (filterRole ? user.role === filterRole : true)
  );

  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value - 1); // API использует 0-based индексацию
  };

  const handleEditUser = (user: UserDto) => {
    if (onEditUser) {
      onEditUser(user);
    }
  };

  const handleDeleteUser = (user: UserDto) => {
    if (onDeleteUser) {
      onDeleteUser(user);
    }
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
          label="Поиск по имени пользователя"
          value={filterUsername}
          onChange={e => setFilterUsername(e.target.value)}
        />
        <TextField
          select
          label="Роль"
          value={filterRole}
          onChange={e => setFilterRole(e.target.value)}
          sx={{ minWidth: 180 }}
        >
          {roles.map(option => (
            <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
          ))}
        </TextField>
      </Box>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Имя пользователя</TableCell>
              <TableCell>Полное имя</TableCell>
              <TableCell>Роль</TableCell>
              <TableCell>Активен</TableCell>
              <TableCell align="center">Действия</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredUsers.map((user) => (
              <TableRow key={user.id}>
                <TableCell>{user.username}</TableCell>
                <TableCell>{user.fullName}</TableCell>
                <TableCell>{translateRole(user.role)}</TableCell>
                <TableCell>{user.enabled ? 'Да' : 'Нет'}</TableCell>
                <TableCell align="center">
                  <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                    <Tooltip title="Редактировать">
                      <IconButton 
                        color="primary" 
                        onClick={() => handleEditUser(user)}
                        size="small"
                      >
                        <EditIcon />
                      </IconButton>
                    </Tooltip>
                    <Tooltip title="Удалить">
                      <IconButton 
                        color="error" 
                        onClick={() => handleDeleteUser(user)}
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

export default UsersTable; 