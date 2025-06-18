# Инструкция по настройке и запуску

## Предварительные требования

1. **Java 17** - убедитесь, что установлена Java 17 или выше
   ```bash
   java -version
   ```

2. **PostgreSQL 15** - установите PostgreSQL 15 или выше
   - Скачайте с официального сайта: https://www.postgresql.org/download/
   - Или используйте Docker: `docker run --name postgres -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres:15`

3. **Docker и Docker Compose** (опционально, для контейнеризованного запуска)

## Настройка базы данных

1. **Создайте базу данных**:
   ```sql
   CREATE DATABASE cartridge_db;
   ```

2. **Создайте пользователя** (опционально):
   ```sql
   CREATE USER postgres WITH PASSWORD 'password';
   GRANT ALL PRIVILEGES ON DATABASE cartridge_db TO postgres;
   ```

3. **Liquibase**
   - Структура базы данных и тестовые данные создаются автоматически при первом запуске приложения с помощью Liquibase (см. changelog-файлы в `src/main/resources/db/changelog/`).

## Настройка приложения

1. **Клонируйте репозиторий**:
   ```bash
   git clone <repository-url>
   cd mfu
   ```

2. **Проверьте настройки** в `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/cartridge_db
       username: postgres
       password: password
   ```

3. **Измените JWT секрет** для продакшена:
   ```yaml
   jwt:
     secret: ваш-очень-длинный-и-безопасный-секрет
   ```

## Запуск приложения

### Вариант 1: Локальный запуск

1. **Соберите проект**:
   ```bash
   ./gradlew build
   ```

2. **Запустите приложение**:
   ```bash
   ./gradlew bootRun
   ```

3. **Проверьте работу**:
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API документация: http://localhost:8080/api-docs

### Вариант 2: Docker Compose

1. **Соберите и запустите**:
   ```bash
   ./gradlew build
   docker-compose up -d
   ```

2. **Проверьте работу**:
   - Swagger UI: http://localhost:8080/swagger-ui.html

### Вариант 3: JAR файл

1. **Соберите JAR**:
   ```bash
   ./gradlew bootJar
   ```

2. **Запустите JAR**:
   ```bash
   java -jar build/libs/cartridge-accounting-0.0.1-SNAPSHOT.jar
   ```

## Тестирование API

### 1. Создание пользователя
```bash
curl -X POST "http://localhost:8080/api/users" \
  -H "Content-Type: application/json" \
  -u "admin:admin" \
  -d '{
    "username": "newuser",
    "password": "password123",
    "fullName": "Новый Пользователь",
    "role": "OBJECT_USER",
    "enabled": true
  }'
```

### 2. Создание объекта
```bash
curl -X POST "http://localhost:8080/api/locations" \
  -H "Content-Type: application/json" \
  -u "admin:admin" \
  -d '{
    "name": "Новый офис",
    "address": "ул. Новая, 15",
    "contactPerson": "Иванов И.И.",
    "contactPhone": "+7-999-123-45-67",
    "description": "Новый офис компании",
    "active": true
  }'
```

### 3. Создание картриджа
```bash
curl -X POST "http://localhost:8080/api/cartridges" \
  -H "Content-Type: application/json" \
  -u "admin:admin" \
  -d '{
    "model": "HP 26A",
    "serialNumber": "HP26A001",
    "resourcePages": 2000,
    "description": "Черный картридж HP LaserJet"
  }'
```

### 4. Получение списка картриджей
```bash
curl -X GET "http://localhost:8080/api/cartridges" \
  -u "admin:admin"
```

### 5. Создание операции
```bash
curl -X POST "http://localhost:8080/api/operations" \
  -H "Content-Type: application/json" \
  -u "admin:admin" \
  -d '{
    "type": "ISSUE",
    "count": 1,
    "cartridgeId": "550e8400-e29b-41d4-a716-446655440201",
    "locationId": "550e8400-e29b-41d4-a716-446655440102",
    "notes": "Выдача в офис"
  }'
```

### 6. Получение пользователей по роли
```bash
curl -X GET "http://localhost:8080/api/users/role/ADMIN" \
  -u "admin:admin"
```

### 7. Получение активных объектов
```bash
curl -X GET "http://localhost:8080/api/locations/active" \
  -u "admin:admin"
```

### 8. Изменение статуса пользователя
```bash
curl -X PATCH "http://localhost:8080/api/users/550e8400-e29b-41d4-a716-446655440001/status?enabled=false" \
  -u "admin:admin"
```

## Структура проекта

```
src/main/java/com/example/cartridgeaccounting/
├── config/           # Конфигурации Spring
├── controller/       # REST контроллеры
├── dto/             # DTO классы
├── entity/          # JPA сущности
├── exception/       # Кастомные исключения
├── repository/      # Репозитории
├── service/         # Сервисы
│   └── impl/       # Реализации сервисов
└── CartridgeAccountingApplication.java
```

## Основные эндпоинты

### Картриджи
- `POST /api/cartridges` - Создать картридж
- `GET /api/cartridges` - Получить все картриджи
- `GET /api/cartridges/{id}` - Получить картридж по ID
- `PUT /api/cartridges/{id}` - Обновить картридж
- `DELETE /api/cartridges/{id}` - Удалить картридж

### Операции
- `POST /api/operations` - Создать операцию
- `GET /api/operations` - Получить все операции
- `GET /api/operations/cartridge/{id}` - Операции по картриджу

### Пользователи
- `POST /api/users` - Создать пользователя
- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{id}` - Получить пользователя по ID
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя
- `PATCH /api/users/{id}/status` - Изменить статус пользователя
- `PATCH /api/users/{id}/password` - Изменить пароль пользователя

### Объекты
- `POST /api/locations` - Создать объект
- `GET /api/locations` - Получить все объекты
- `GET /api/locations/{id}` - Получить объект по ID
- `PUT /api/locations/{id}` - Обновить объект
- `DELETE /api/locations/{id}` - Удалить объект

## Роли пользователей

- **ADMIN** - полный доступ
- **WAREHOUSE_MANAGER** - управление складом
- **OBJECT_USER** - просмотр информации

## Логирование

Логи приложения сохраняются в консоль. Для продакшена рекомендуется настроить файловое логирование:

```yaml
logging:
  file:
    name: logs/cartridge-accounting.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

## Мониторинг

Для мониторинга приложения можно использовать:
- Spring Boot Actuator (добавить зависимость)
- Prometheus + Grafana
- ELK Stack

## Безопасность

1. **Измените пароли** по умолчанию
2. **Настройте HTTPS** для продакшена
3. **Ограничьте доступ** к базе данных
4. **Настройте брандмауэр**

## Устранение неполадок

### Ошибка подключения к базе данных
- Проверьте, что PostgreSQL запущен
- Проверьте настройки подключения в `application.yml`
- Убедитесь, что база данных создана

### Ошибка порта
- Измените порт в `application.yml`: `server.port: 8081`
- Или остановите процесс, использующий порт 8080

### Ошибка сборки
- Убедитесь, что установлена Java 17
- Очистите кэш: `./gradlew clean`
- Пересоберите: `./gradlew build`

### Ошибка аутентификации
- Проверьте логин и пароль в запросах
- Убедитесь, что пользователь существует и активен
- Проверьте права доступа пользователя

## Поддержка

При возникновении проблем:
1. Проверьте логи приложения
2. Обратитесь к документации API (Swagger UI)
3. Создайте issue в репозитории 