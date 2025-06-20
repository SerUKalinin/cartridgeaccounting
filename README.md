# Система учёта картриджей МФУ

Веб-приложение для автоматизации учёта картриджей для многофункциональных устройств (МФУ). Система позволяет вести учёт поступления, выдачи, отправки на заправку и списания картриджей на различных объектах.

## 🚀 Возможности

- **Управление картриджами**: создание, редактирование, удаление картриджей
- **Операции с картриджами**: поступление, выдача, возврат, заправка, списание
- **Учёт по объектам**: отслеживание остатков на различных объектах
- **История операций**: полная история всех операций с картриджами
- **Роли пользователей**: администратор, складской работник, пользователь объекта
- **REST API**: полное API для интеграции с другими системами
- **Swagger документация**: автоматическая документация API

## 🛠 Технологический стек

- **Backend**: Spring Boot 3.2.0, Java 17
- **База данных**: PostgreSQL 15
- **ORM**: Hibernate/JPA
- **Безопасность**: Spring Security (Basic Auth)
- **Документация**: Swagger/OpenAPI 3
- **Сборка**: Gradle
- **Контейнеризация**: Docker, Docker Compose

## 📋 Требования

- Java 17 или выше
- PostgreSQL 15 или выше
- Docker и Docker Compose (для контейнеризованного развертывания)

## 🚀 Быстрый старт

### Вариант 1: Локальный запуск

1. **Клонируйте репозиторий**:
   ```bash
   git clone <repository-url>
   cd mfu
   ```

2. **Настройте базу данных PostgreSQL**:
   ```sql
   -- Создайте базу данных
   CREATE DATABASE cartridge_db;
   
   -- Подключитесь к базе данных
   \c cartridge_db;
   ```

3. **Обновите настройки в `src/main/resources/application.yml`**:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/cartridge_db
       username: postgres
       password: your_password
   ```

4. **Liquibase**
   - Структура базы данных и тестовые данные создаются автоматически при первом запуске приложения с помощью Liquibase (см. changelog-файлы в `src/main/resources/db/changelog/`).

5. **Запустите приложение**:
   ```bash
   ./gradlew bootRun
   ```

6. **Откройте Swagger UI**:
   ```
   http://localhost:8080/swagger-ui.html
   ```

### Вариант 2: Docker Compose

1. **Клонируйте репозиторий**:
   ```bash
   git clone <repository-url>
   cd mfu
   ```

2. **Соберите и запустите с Docker Compose**:
   ```bash
   ./gradlew build
   docker-compose up -d
   ```

3. **Откройте Swagger UI**:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## 🔐 Аутентификация

Система использует **Basic Authentication** (логин/пароль).

### Тестовые пользователи:
- **admin** / **admin123** - Администратор (полный доступ)
- **warehouse** / **warehouse123** - Складской работник
- **user1** / **user123** - Пользователь объекта

## 📊 Структура базы данных

### Основные сущности:

- **Cartridge** - картриджи (модель, серийный номер, статус, ресурс)
- **Location** - объекты/места хранения (название, адрес, контактное лицо)
- **Operation** - операции с картриджами (тип, количество, дата, исполнитель)
- **User** - пользователи системы (логин, пароль, роль)

### Статусы картриджей:
- `IN_STOCK` - На складе
- `IN_USE` - В использовании
- `REFILLING` - На заправке
- `DISPOSED` - Списан

### Типы операций:
- `RECEIPT` - Поступление
- `ISSUE` - Выдача
- `RETURN` - Возврат
- `REFILL` - Заправка
- `DISPOSAL` - Списание

## 🔐 Роли пользователей

- **ADMIN** - полный доступ ко всем функциям
- **WAREHOUSE_MANAGER** - управление складом, операции с картриджами
- **OBJECT_USER** - просмотр информации, запросы на выдачу

## 📚 API Endpoints

### Картриджи
- `POST /api/cartridges` - Создать картридж
- `GET /api/cartridges` - Получить все картриджи
- `GET /api/cartridges/{id}` - Получить картридж по ID
- `GET /api/cartridges/serial/{serialNumber}` - Получить по серийному номеру
- `PUT /api/cartridges/{id}` - Обновить картридж
- `DELETE /api/cartridges/{id}` - Удалить картридж

### Пользователи
- `POST /api/users` - Создать пользователя
- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{id}` - Получить пользователя по ID
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

### Объекты
- `POST /api/locations` - Создать объект
- `GET /api/locations` - Получить все объекты
- `GET /api/locations/{id}` - Получить объект по ID
- `PUT /api/locations/{id}` - Обновить объект
- `DELETE /api/locations/{id}` - Удалить объект

## 🐛 Обработка ошибок

Система включает кастомные исключения:
- `CartridgeNotFoundException` - картридж не найден
- `LocationNotFoundException` - объект не найден
- `UserNotFoundException` - пользователь не найден
- `DuplicateSerialNumberException` - дублирование серийного номера
- `DuplicateUsernameException` - дублирование имени пользователя

## 📝 Логирование

Приложение использует SLF4J с настройками логирования в `application.yml`. Логи включают:
- SQL запросы (в режиме DEBUG)
- Операции с картриджами
- Ошибки безопасности
- Общие ошибки приложения

## 🔧 Конфигурация

Основные настройки в `src/main/resources/application.yml`:
- Настройки базы данных
- Swagger конфигурация
- Логирование
- Безопасность

## 🚀 Развертывание

### Продакшн настройки:
1. Настройте безопасные пароли для базы данных
2. Измените пароли пользователей по умолчанию
3. Включите HTTPS
4. Настройте мониторинг и логирование

### Docker развертывание:
```bash
# Сборка образа
docker build -t cartridge-accounting .

# Запуск контейнера
docker run -p 8080:8080 cartridge-accounting
```

## 🤝 Вклад в проект

1. Форкните репозиторий
2. Создайте ветку для новой функции
3. Внесите изменения
4. Создайте Pull Request

## 📄 Лицензия

MIT License - см. файл LICENSE для деталей.

## 📞 Поддержка

По вопросам и предложениям обращайтесь:
- Email: developer@example.com
- Issues: GitHub Issues

---

**Версия**: 1.0.0  
**Дата**: 2024 