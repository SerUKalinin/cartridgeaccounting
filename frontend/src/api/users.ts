import api from './index';

export interface UserDto {
  id: string;
  username: string;
  fullName: string;
  role: string;
  enabled: boolean;
}

export interface CreateUserRequest {
  username: string;
  password: string;
  fullName: string;
  role: string;
}

export interface UpdateUserRequest {
  fullName?: string;
  role?: string;
  enabled?: boolean;
  password?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

// Получить всех пользователей с пагинацией
export async function getUsers(page: number = 0, size: number = 10): Promise<PageResponse<UserDto>> {
  const response = await api.get<PageResponse<UserDto>>(`/api/users?page=${page}&size=${size}`);
  return response.data;
}

// Получить пользователя по ID
export async function getUserById(id: string): Promise<UserDto> {
  const response = await api.get<UserDto>(`/api/users/${id}`);
  return response.data;
}

// Создать пользователя
export async function createUser(data: CreateUserRequest): Promise<UserDto> {
  const response = await api.post<UserDto>('/api/users', data);
  return response.data;
}

// Обновить пользователя
export async function updateUser(id: string, data: UpdateUserRequest): Promise<UserDto> {
  const response = await api.put<UserDto>(`/api/users/${id}`, data);
  return response.data;
}

// Удалить пользователя
export async function deleteUser(id: string): Promise<void> {
  await api.delete(`/api/users/${id}`);
}

// Изменить статус пользователя
export async function changeUserStatus(id: string, enabled: boolean): Promise<void> {
  await api.patch(`/api/users/${id}/status?enabled=${enabled}`);
}

// Изменить пароль пользователя
export async function changeUserPassword(id: string, newPassword: string): Promise<void> {
  await api.patch(`/api/users/${id}/password?newPassword=${newPassword}`);
}

// Проверить существование пользователя
export async function checkUserExists(username: string): Promise<boolean> {
  const response = await api.get<boolean>(`/api/users/exists/${username}`);
  return response.data;
} 