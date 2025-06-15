СТО (Сервис Технического Обслуживания) - Backend приложение
Описание проекта
Проект представляет собой REST API для управления заявками в сервисном техническом обслуживании. Основные возможности:

Создание и управление заявками

Отслеживание истории изменения статусов

Уведомление клиентов

Интеграция с Kafka для обработки событий

Технологический стек
Java 21

Spring Boot 3.4.2

PostgreSQL

Kafka (опционально)

Redis (кеширование)

Docker

Lombok

MapStruct

Требования
JDK 21+

Maven 3.9+

Docker 20.10+

Docker Compose 2.20+

Установка и запуск
1. Сборка проекта
bash
mvn clean package
2. Запуск через Docker Compose
bash
docker-compose up --build
3. Запуск без Docker
bash
mvn spring-boot:run
Конфигурация
Основные настройки в application.properties:

properties
# DB
spring.datasource.url=jdbc:postgresql://db:5432/sto_db
spring.datasource.username=user
spring.datasource.password=password

# Kafka (опционально)
spring.kafka.bootstrap-servers=kafka:9092

# Redis
spring.data.redis.host=redis
spring.cache.type=redis
API Endpoints
Заявки
POST /api/requests - Создать новую заявку

GET /api/requests/{id} - Получить заявку по ID

PUT /api/requests/{id}/status - Изменить статус заявки

GET /api/requests/client/{phone} - Получить заявки клиента

История статусов
GET /api/requests/{id}/history - Получить историю изменений статуса

Примеры запросов
Создание заявки
bash
curl -X POST http://localhost:8080/api/requests \
  -H "Content-Type: application/json" \
  -d '{
    "clientName": "Иван Иванов",
    "clientPhone": "+77771234567",
    "problemDescription": "Не работает кондиционер"
  }'
Изменение статуса
bash
curl -X PUT http://localhost:8080/api/requests/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "newStatus": "IN_PROGRESS",
    "changeReason": "Назначен мастер",
    "changedBy": "Менеджер Петров"
  }'
Структура проекта
text
src/
├── main/
│   ├── java/
│   │   └── com/nurtikaga/CTO/
│   │       ├── config/       # Конфигурационные классы
│   │       ├── controller/   # Контроллеры
│   │       ├── dto/          # Data Transfer Objects
│   │       ├── exception/    # Кастомные исключения
│   │       ├── model/        # Сущности БД
│   │       ├── repository/   # Репозитории
│   │       ├── service/      # Бизнес-логика
│   │       └── CtoApplication.java
│   └── resources/
│       └── application.properties
├── test/                     # Тесты
Тестирование
Запуск тестов:

bash
mvn test
Настройка окружения разработки
Установите плагины для IDE:

Lombok

MapStruct

Spring Boot Tools

Рекомендуемые настройки:

properties
# В application-dev.properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.com.nurtikaga=DEBUG
Логирование
Логи хранятся в:

logs/application.log (основной лог)

Консоль Docker при использовании контейнеров

Мониторинг
Actuator: http://localhost:8080/actuator

Health check: http://localhost:8080/actuator/health

Metrics: http://localhost:8080/actuator/metrics

Лицензия
MIT License
