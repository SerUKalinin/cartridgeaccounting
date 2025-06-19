import React, { useState, useCallback } from 'react';
import { Box, Button, Typography, Snackbar, Alert } from '@mui/material';
import LocationsTable from '../components/locations/LocationsTable';
import LocationFormDialog from '../components/locations/LocationFormDialog';
import DeleteLocationDialog from '../components/locations/DeleteLocationDialog';
import { LocationDto, deleteLocation } from '../api/locations';

const LocationsPage: React.FC = () => {
  const [openForm, setOpenForm] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState<LocationDto | null>(null);
  const [refreshKey, setRefreshKey] = useState(0);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [deleteError, setDeleteError] = useState<string | null>(null);
  const [snackbar, setSnackbar] = useState<{
    open: boolean;
    message: string;
    severity: 'success' | 'error';
  }>({ open: false, message: '', severity: 'success' });

  const handleOpenForm = () => setOpenForm(true);
  const handleCloseForm = () => setOpenForm(false);

  const handleOpenDelete = (location: LocationDto) => {
    setSelectedLocation(location);
    setOpenDelete(true);
  };

  const handleCloseDelete = () => {
    setOpenDelete(false);
    setSelectedLocation(null);
    setDeleteError(null);
  };

  const handleSuccess = useCallback(() => {
    setRefreshKey(prev => prev + 1);
    setSnackbar({
      open: true,
      message: selectedLocation ? 'Объект успешно обновлен' : 'Объект успешно добавлен',
      severity: 'success'
    });
  }, [selectedLocation]);

  const handleEditLocation = (location: LocationDto) => {
    setSelectedLocation(location);
    setOpenForm(true);
  };

  const handleDeleteLocation = (location: LocationDto) => {
    handleOpenDelete(location);
  };

  const handleConfirmDelete = async () => {
    if (!selectedLocation) return;

    try {
      setDeleteLoading(true);
      setDeleteError(null);
      await deleteLocation(selectedLocation.id);
      setRefreshKey(prev => prev + 1);
      setSnackbar({
        open: true,
        message: 'Объект успешно удален',
        severity: 'success'
      });
      handleCloseDelete();
    } catch (err: any) {
      setDeleteError(err.response?.data?.message || 'Ошибка удаления объекта');
      setSnackbar({
        open: true,
        message: 'Ошибка удаления объекта',
        severity: 'error'
      });
    } finally {
      setDeleteLoading(false);
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4">Объекты</Typography>
        <Button variant="contained" onClick={handleOpenForm}>Добавить объект</Button>
      </Box>
      
      <LocationsTable 
        key={refreshKey} 
        onEditLocation={handleEditLocation}
        onDeleteLocation={handleDeleteLocation}
      />
      
      <LocationFormDialog 
        open={openForm} 
        onClose={handleCloseForm} 
        onSuccess={handleSuccess}
        location={selectedLocation}
      />
      
      <DeleteLocationDialog
        open={openDelete}
        location={selectedLocation}
        onClose={handleCloseDelete}
        onConfirm={handleConfirmDelete}
        loading={deleteLoading}
        error={deleteError}
      />

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
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

export default LocationsPage; 