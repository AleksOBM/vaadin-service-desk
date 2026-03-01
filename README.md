## Service Desk

Это приложение для работы с базой данных небольшой компании, занимающейся обслуживанием заявок клиентов.

### Основные функции
- Учет сотрудников
- Учет клиентов
- Учет заявок от клиентов и какой сотрудник ведет заявку
- Корзина для удаленных элементов с возможностью восстановления
- Дашборд со статистикой заявок
- Функция отправки оповещений по электронной почте при создании новой заявки

### Требования (Environmental Requirements)
Для запуска проекта требуется: - Java версии 21  
Приложение работает на порту указанном в файле application.yaml

### Запуск приложения на сервере (Running the Application)
Откройте терминал в корне проекта и выполните команды для запуска приложения.

**Вариант 1 Spring**  
`mvn spring-boot:run`
- для остановки CTRL+C

**Вариант 2 Java**  
`mvn clean`  
`mvn package`     
`cd target`  
`java -jar *.jar`  
- для остановки CTRL+C

**Вариант 3 Docker**   
`mvn package`  
`sudo docker build -t service-desk-image .`  
`sudo docker run --name service-desk -p 8080:8080 service-desk-image`  
- для остановки `sudo docker stop service-desk`  

### Работа с приложением (Working with the app)
Для работы с приложением используйте любой браузер или веб-клиент  

### Структура проекта (Structure)

Веб-приложения Vaadin являются полнофункциональными и включают в себя как клиентский, так и серверный код в одном проекте.

| Directory                                  | Description |
|:-------------------------------------------| :--- |
| `src/main/java/com/example/application/`                 | Server-side source directory |
| &nbsp;&nbsp;&nbsp;&nbsp;`ServiceDeskApplication.java` | Server entrypoint |

### Схема базы данных (Database map)
![DatabaseMap](database-map.png "Database map:")

### Архитектура проекта (Project Architecture)

Проект представляет собой монолитное веб-приложение на Java, построенное с использованием Spring Boot и Vaadin.
(The project is a monolithic Java web application built using Spring Boot and Vaadin.)

Используется слоистая архитектура, разделяющая пользовательский интерфейс, бизнес-логику и слой доступа к данным.
(A layered architecture is used to separate UI, business logic, and data access.)

#### Технологический стек (Technology Stack)
- Java 21 — основной язык разработки
(main programming language)
- Spring Boot 4.0.2 — фреймворк приложения и DI-контейнер
(application framework and dependency injection)
- Vaadin 25.0.4 — серверный UI-фреймворк
(server-side web UI framework)
- Spring Data JPA — абстракция слоя доступа к данным
(persistence layer abstraction)
- Hibernate (через JPA) — ORM
(ORM implementation)
- H2 Database — встраиваемая реляционная БД
(embedded relational database)
- Bean Validation (Jakarta Validation) — валидация данных
(input and entity validation)
- Lombok — уменьшение шаблонного кода
(boilerplate code reduction)
- Vaadin Add-ons — дополнительные UI-компоненты
(additional UI components)

#### UI слой — Представление (UI Layer / Presentation)
- Отображение интерфейса
(rendering UI)
- Обработка пользовательских событий
(handling user input and events)
- Вызов сервисного слоя
(triggering service layer operations)
- Валидация форм
(form validation)

#### Сервисный слой — Бизнес-логика (Service Layer / Business Logic)
- Обработка пользовательских действий
(processing user actions)
- Работа с репозиториями
(coordinating repositories)
- Проверка бизнес-правил
(enforcing business rules)
- Отправка email-уведомлений
(sending emails)

#### Слой доступа к данным (Persistence Layer)
- CRUD-операции
(CRUD operations)
- SQL-запросы
(query execution)
- Маппинг сущностей
(entity mapping)

#### Доменная модель (Domain Model)
- Отражение таблиц БД
(representing database tables)
- Правила валидации
(validation rules)
- Связи между сущностями
(entity relationships)

#### Поток выполнения (Application Flow)
```plain-text
User → Vaadin UI → Service Layer → Repository (JPA) → Database
           ↑
Validation & Business Logic
```

#### Тестирование (Testing)
- Spring Boot Test — интеграционные тесты
(integration testing)
- Vaadin TestBench — UI-тесты
(UI testing)
- H2 Database — тестовая БД
(test database)

#### Сборка и конфигурация (Build & Configuration)
- Maven — система сборки
(build tool)
- Vaadin BOM — управление версиями
(version alignment)
- Spring Boot DevTools — горячая перезагрузка
(hot reload during development)

#### Модель деплоя (Deployment Model)
- Приложение собирается в исполняемый JAR
(packaged as executable JAR)
- Используется встроенный сервер (Tomcat)
(embedded server)
- Backend и UI развёртываются вместе
(backend and UI deployed together)

#### Архитектурный стиль (Architectural Style)
- Монолитная архитектура (monolithic)  
- Слоистая структура (layered architecture)
- Серверная генерация UI (server-side rendering)
- Типобезопасный UI (strongly typed UI)

### Список литературы (Useful links)
- [Practical Vaadin: Developing Web Applications in Java](https://www.amazon.com/dp/B09BYC4QWD?tag=2c68ca2-20&linkCode=ogi&th=1&psc=1)
- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
- Follow the tutorials at [vaadin.com/tutorials](https://vaadin.com/tutorials).
- Watch training videos and get certified at [vaadin.com/learn/training](https://vaadin.com/learn/training).
- Create new projects at [start.vaadin.com](https://start.vaadin.com/).
- Search UI components and their usage examples at [vaadin.com/components](https://vaadin.com/components).
- View use case applications that demonstrate Vaadin capabilities at [vaadin.com/examples-and-demos](https://vaadin.com/examples-and-demos).
- Discover Vaadin's set of CSS utility classes that enable building any UI without custom CSS in the [docs](https://vaadin.com/docs/latest/ds/foundation/utility-classes). 
- Find a collection of solutions to common use cases in [Vaadin Cookbook](https://cookbook.vaadin.com/).
- Find Add-ons at [vaadin.com/directory](https://vaadin.com/directory).
- Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/vaadin) or join our [Discord channel](https://discord.gg/MYFq5RTbBn).
- Report issues, create pull requests in [GitHub](https://github.com/vaadin/platform).
