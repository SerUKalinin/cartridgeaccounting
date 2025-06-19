import api from './index';

export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  role: string;
}

export interface CreateUserRequest {
  username: string;
  password: string;
  fullName: string;
  role: string;
}

export async function login(data: AuthRequest): Promise<AuthResponse> {
  const response = await api.post<AuthResponse>('/api/auth/login', data);
  return response.data;
}

export async function register(data: CreateUserRequest): Promise<void> {
  await api.post('/api/auth/register', data);
} 