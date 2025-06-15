# СТО 
(Сервис Технического Обслуживания) - Backend приложение
Описание проекта
Проект представляет собой REST API для управления заявками в сервисном техническом обслуживании. Основные возможности:

1. Создание и управление заявками
2. Отслеживание истории изменения статусов
3. Уведомление клиентов
4. Интеграция с Kafka для обработки событий

Технологический стек:
1. Java 21
2. Spring Boot 3.4.2
3. PostgreSQL
4. Kafka
5. Redis (кеширование)
6. Docker
7. Lombok
8. MapStruct;
   
В планах добавить еще Swagger, по причине переезда, не успел доделать.


## Установка и запуск
1. Сборка проекта
```
bash
mvn clean package
```
2. Запуск через Docker Compose
```
bash
docker-compose up --build
```

4. Запуск без Docker
```
bash
mvn spring-boot:run
```

## Основные настройки в application.properties:

### DB
spring.datasource.url=jdbc:postgresql://db:5432/cto_db
spring.datasource.username=user
spring.datasource.password=password

### Kafka (опционально)
spring.kafka.bootstrap-servers=kafka:9092

### Redis
spring.data.redis.host=redis
spring.cache.type=redis

## API Endpoints
1. Заявки
POST /api/requests - Создать новую заявку
GET /api/requests/{id} - Получить заявку по ID
PUT /api/requests/{id}/status - Изменить статус заявки
GET /api/requests/client/{phone} - Получить заявки клиента

2. История статусов
GET /api/requests/{id}/history - Получить историю изменений статуса

### Примеры запросов
Создание заявки
```
bash
curl -X POST http://localhost:8080/api/requests \
  -H "Content-Type: application/json" \
  -d '{
    "clientName": "Иван Иванов",
    "clientPhone": "+77771234567",
    "problemDescription": "Не работает кондиционер"
  }'
```
3. Изменение статуса
```
bash
curl -X PUT http://localhost:8080/api/requests/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "newStatus": "IN_PROGRESS",
    "changeReason": "Назначен мастер",
    "changedBy": "Менеджер Петров"
  }'
```
## Структура проекта
```
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
```
## Тестирование
Запуск тестов:
```
bash
mvn test
```

## Настройка окружения разработки
Установите плагины для IDE:

1. Lombok
2. MapStruct
3. Spring Boot Tools

## Рекомендуемые настройки:
### В application-dev.properties
1. spring.jpa.show-sql=true
2. spring.jpa.properties.hibernate.format_sql=true
3. logging.level.com.nurtikaga=DEBUG

По любым вопросам или рекомендациям, ю ар велком, тг: @nrtagd
