import axios from 'axios';

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
  role: string; // ADMIN, USER, etc.
}

export async function login(data: AuthRequest): Promise<AuthResponse> {
  const response = await axios.post<AuthResponse>('/api/auth/login', data);
  return response.data;
}

export async function register(data: CreateUserRequest): Promise<void> {
  await axios.post('/api/auth/register', data);
} 