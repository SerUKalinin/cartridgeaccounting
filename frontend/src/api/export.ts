import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Функция для скачивания файла
const downloadFile = (blob: Blob, filename: string) => {
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(url);
};

// Экспорт картриджей
export const exportCartridges = async (status?: string, model?: string) => {
  try {
    const params = new URLSearchParams();
    if (status) params.append('status', status);
    if (model) params.append('model', model);

    const response = await axios.get(`${API_BASE_URL}/export/cartridges?${params}`, {
      responseType: 'blob',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const filename = `cartridges_${new Date().toISOString().slice(0, 19).replace(/:/g, '-')}.xlsx`;
    downloadFile(response.data, filename);
    return true;
  } catch (error) {
    console.error('Error exporting cartridges:', error);
    throw error;
  }
};

// Экспорт операций
export const exportOperations = async (type?: string, startDate?: string, endDate?: string) => {
  try {
    const params = new URLSearchParams();
    if (type) params.append('type', type);
    if (startDate) params.append('startDate', startDate);
    if (endDate) params.append('endDate', endDate);

    const response = await axios.get(`${API_BASE_URL}/export/operations?${params}`, {
      responseType: 'blob',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const filename = `operations_${new Date().toISOString().slice(0, 19).replace(/:/g, '-')}.xlsx`;
    downloadFile(response.data, filename);
    return true;
  } catch (error) {
    console.error('Error exporting operations:', error);
    throw error;
  }
};

// Экспорт пользователей
export const exportUsers = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/export/users`, {
      responseType: 'blob',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const filename = `users_${new Date().toISOString().slice(0, 19).replace(/:/g, '-')}.xlsx`;
    downloadFile(response.data, filename);
    return true;
  } catch (error) {
    console.error('Error exporting users:', error);
    throw error;
  }
};

// Экспорт объектов
export const exportLocations = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/export/locations`, {
      responseType: 'blob',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const filename = `locations_${new Date().toISOString().slice(0, 19).replace(/:/g, '-')}.xlsx`;
    downloadFile(response.data, filename);
    return true;
  } catch (error) {
    console.error('Error exporting locations:', error);
    throw error;
  }
};

// Экспорт всех данных
export const exportAll = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/export/all`, {
      responseType: 'blob',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const filename = `cartridge_accounting_${new Date().toISOString().slice(0, 19).replace(/:/g, '-')}.xlsx`;
    downloadFile(response.data, filename);
    return true;
  } catch (error) {
    console.error('Error exporting all data:', error);
    throw error;
  }
}; 