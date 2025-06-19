import api from './index';

export interface OperationDto {
  id: string;
  type: string;
  count: number;
  cartridgeId: string;
  cartridgeModel?: string;
  cartridgeSerialNumber?: string;
  locationId?: string;
  locationName?: string;
  performedBy: string;
  performedByName?: string;
  operationDate: string;
  notes?: string;
}

export interface CreateOperationRequest {
  type: string;
  count: number;
  cartridgeId: string;
  locationId?: string;
  notes?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

// Получить все операции с пагинацией
export async function getOperations(page: number = 0, size: number = 10): Promise<PageResponse<OperationDto>> {
  const response = await api.get<PageResponse<OperationDto>>(`/api/operations?page=${page}&size=${size}`);
  return response.data;
}

// Получить операцию по ID
export async function getOperationById(id: string): Promise<OperationDto> {
  const response = await api.get<OperationDto>(`/api/operations/${id}`);
  return response.data;
}

// Создать операцию
export async function createOperation(data: CreateOperationRequest): Promise<OperationDto> {
  const response = await api.post<OperationDto>('/api/operations', data);
  return response.data;
}

// Получить операции по картриджу
export async function getOperationsByCartridge(cartridgeId: string): Promise<OperationDto[]> {
  const response = await api.get<OperationDto[]>(`/api/operations/cartridge/${cartridgeId}`);
  return response.data;
}

// Получить операции по объекту
export async function getOperationsByLocation(locationId: string): Promise<OperationDto[]> {
  const response = await api.get<OperationDto[]>(`/api/operations/location/${locationId}`);
  return response.data;
}

// Получить операции по типу
export async function getOperationsByType(type: string): Promise<OperationDto[]> {
  const response = await api.get<OperationDto[]>(`/api/operations/type/${type}`);
  return response.data;
}

// Получить операции по дате
export async function getOperationsByDate(
  startDate: string, 
  endDate: string
): Promise<OperationDto[]> {
  const response = await api.get<OperationDto[]>(`/api/operations/date?startDate=${startDate}&endDate=${endDate}`);
  return response.data;
}

// Получить операции по пользователю
export async function getOperationsByUser(userId: string): Promise<OperationDto[]> {
  const response = await api.get<OperationDto[]>(`/api/operations/user/${userId}`);
  return response.data;
}

// Поиск операций
export async function searchOperations(
  type?: string,
  cartridgeId?: string,
  locationId?: string,
  startDate?: string,
  endDate?: string,
  page: number = 0,
  size: number = 10
): Promise<PageResponse<OperationDto>> {
  const params = new URLSearchParams();
  if (type) params.append('type', type);
  if (cartridgeId) params.append('cartridgeId', cartridgeId);
  if (locationId) params.append('locationId', locationId);
  if (startDate) params.append('startDate', startDate);
  if (endDate) params.append('endDate', endDate);
  params.append('page', page.toString());
  params.append('size', size.toString());
  
  const response = await api.get<PageResponse<OperationDto>>(`/api/operations/search?${params}`);
  return response.data;
} 