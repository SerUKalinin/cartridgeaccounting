import api from './index';

export interface CartridgeDto {
  id: string;
  model: string;
  serialNumber?: string;
  resourcePages?: number;
  description?: string;
  status: string;
  currentLocationId?: string;
  currentLocationName?: string;
  createdAt: string;
  updatedAt?: string;
  brand?: string;
  partNumber?: string;
  color?: string;
  compatiblePrinters?: string;
}

export interface CreateCartridgeRequest {
  model: string;
  serialNumber?: string;
  resourcePages?: number;
  description?: string;
  status: string;
  currentLocationId?: string;
  brand?: string;
  partNumber?: string;
  color?: string;
  compatiblePrinters?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

// Получить все картриджи с пагинацией
export async function getCartridges(page: number = 0, size: number = 10): Promise<PageResponse<CartridgeDto>> {
  const response = await api.get<PageResponse<CartridgeDto>>(`/api/cartridges?page=${page}&size=${size}`);
  return response.data;
}

// Получить картридж по ID
export async function getCartridgeById(id: string): Promise<CartridgeDto> {
  const response = await api.get<CartridgeDto>(`/api/cartridges/${id}`);
  return response.data;
}

// Получить картридж по серийному номеру
export async function getCartridgeBySerialNumber(serialNumber: string): Promise<CartridgeDto> {
  const response = await api.get<CartridgeDto>(`/api/cartridges/serial/${serialNumber}`);
  return response.data;
}

// Создать картридж
export async function createCartridge(data: CreateCartridgeRequest): Promise<CartridgeDto> {
  const response = await api.post<CartridgeDto>('/api/cartridges', data);
  return response.data;
}

// Обновить картридж
export async function updateCartridge(id: string, data: CreateCartridgeRequest): Promise<CartridgeDto> {
  const response = await api.put<CartridgeDto>(`/api/cartridges/${id}`, data);
  return response.data;
}

// Удалить картридж
export async function deleteCartridge(id: string): Promise<void> {
  await api.delete(`/api/cartridges/${id}`);
}

// Поиск картриджей
export async function searchCartridges(
  model?: string, 
  serialNumber?: string, 
  page: number = 0, 
  size: number = 10
): Promise<PageResponse<CartridgeDto>> {
  const params = new URLSearchParams();
  if (model) params.append('model', model);
  if (serialNumber) params.append('serialNumber', serialNumber);
  params.append('page', page.toString());
  params.append('size', size.toString());
  
  const response = await api.get<PageResponse<CartridgeDto>>(`/api/cartridges/search?${params}`);
  return response.data;
}

// Получить картриджи по статусу
export async function getCartridgesByStatus(status: string): Promise<CartridgeDto[]> {
  const response = await api.get<CartridgeDto[]>(`/api/cartridges/status/${status}`);
  return response.data;
}

// Получить картриджи по объекту
export async function getCartridgesByLocation(locationId: string): Promise<CartridgeDto[]> {
  const response = await api.get<CartridgeDto[]>(`/api/cartridges/location/${locationId}`);
  return response.data;
}

// Получить количество картриджей по статусу
export async function getCartridgeCountByStatus(status: string): Promise<number> {
  const response = await api.get<number>(`/api/cartridges/count/status/${status}`);
  return response.data;
} 