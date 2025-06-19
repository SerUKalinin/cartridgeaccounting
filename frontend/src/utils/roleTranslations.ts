export const roleTranslations: Record<string, string> = {
  'ADMIN': 'Администратор',
  'WAREHOUSE_MANAGER': 'Заведующий складом',
  'OBJECT_USER': 'Пользователь объекта'
};

/**
 * Переводит роль пользователя на русский язык
 * @param role - роль на английском языке
 * @returns роль на русском языке
 */
export function translateRole(role: string): string {
  return roleTranslations[role] || role;
}

/**
 * Получает список всех ролей с русскими названиями
 * @returns массив объектов с value и label
 */
export function getRoleOptions() {
  return [
    { value: 'ADMIN', label: 'Администратор' },
    { value: 'WAREHOUSE_MANAGER', label: 'Заведующий складом' },
    { value: 'OBJECT_USER', label: 'Пользователь объекта' }
  ];
} 