import api from './index';

export interface LocationDto {
  id: string;
  name: string;
  address: string;
  cabinet?: string;
  contactPerson?: string;
  contactPhone?: string;
  description?: string;
  active: boolean;
  cartridgeCount: number;
}

export interface CreateLocationRequest {
  name: string;
  address: string;
  cabinet?: string;
  contactPerson?: string;
  contactPhone?: string;
  description?: string;
  active?: boolean;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

// Получить все объекты с пагинацией
export async function getLocations(page: number = 0, size: number = 10): Promise<PageResponse<LocationDto>> {
  const response = await api.get<PageResponse<LocationDto>>(`/api/locations?page=${page}&size=${size}`);
  return response.data;
}

// Получить объект по ID
export async function getLocationById(id: string): Promise<LocationDto> {
  const response = await api.get<LocationDto>(`/api/locations/${id}`);
  return response.data;
}

// Создать объект
export async function createLocation(data: CreateLocationRequest): Promise<LocationDto> {
  const response = await api.post<LocationDto>('/api/locations', data);
  return response.data;
}

// Обновить объект
export async function updateLocation(id: string, data: CreateLocationRequest): Promise<LocationDto> {
  const response = await api.put<LocationDto>(`/api/locations/${id}`, data);
  return response.data;
}

// Удалить объект
export async function deleteLocation(id: string): Promise<void> {
  await api.delete(`/api/locations/${id}`);
}

// Поиск объектов
export async function searchLocations(
  name?: string, 
  address?: string, 
  page: number = 0, 
  size: number = 10
): Promise<PageResponse<LocationDto>> {
  const params = new URLSearchParams();
  if (name) params.append('name', name);
  if (address) params.append('address', address);
  params.append('page', page.toString());
  params.append('size', size.toString());
  
  const response = await api.get<PageResponse<LocationDto>>(`/api/locations/search?${params}`);
  return response.data;
}

// Получить активные объекты
export async function getActiveLocations(): Promise<LocationDto[]> {
  const response = await api.get<LocationDto[]>('/api/locations/active');
  return response.data;
}

// Изменить статус объекта
export async function changeLocationStatus(id: string, active: boolean): Promise<void> {
  await api.patch(`/api/locations/${id}/status?active=${active}`);
} 