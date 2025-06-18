# Этап сборки
FROM gradle:7.6.1-jdk17 AS build

WORKDIR /app

# Копируем файлы проекта
COPY build.gradle .
COPY gradle/ gradle/
COPY gradlew .
COPY src/ src/

# Собираем проект без тестов
RUN ./gradlew build --no-daemon -x test

# Этап запуска
FROM openjdk:17-jdk-slim

WORKDIR /app

# Копируем JAR файл из этапа сборки
COPY --from=build /app/build/libs/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"] 