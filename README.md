## Service Desk

![Static Badge](https://img.shields.io/badge/Java-25-green)
![Static Badge](https://img.shields.io/badge/Spring_Boot-4.1.1-green)
![Static Badge](https://img.shields.io/badge/Vaadin-25.2.9-blue)
![Static Badge](https://img.shields.io/badge/PostgreSQL-17-blue)
![Static Badge](https://img.shields.io/badge/Hibernate-4e7a62)
![Static Badge](https://img.shields.io/badge/Flyway-CC0202)
![Static Badge](https://img.shields.io/badge/gRPC-4e7a62)
![Static Badge](https://img.shields.io/badge/Protobuf-blue)
![Static Badge](https://img.shields.io/badge/Spring_Security-487d1f)
![Static Badge](https://img.shields.io/badge/SSL-8512cb)
![Static Badge](https://img.shields.io/badge/HTTPS-a54a4a)
![Static Badge](https://img.shields.io/badge/Maven-orange)
![Static Badge](https://img.shields.io/badge/Docker-blue)

---

<span>
Русский | <a href="./README.en.md">English</a>
</span>

---

Распределённое веб-приложение SPA + API для учёта заказов, клиентов, сотрудников, а также осуществления трёхступенчатого контроля дедлайнов.

### Скриншоты пользовательского интерфейса

<details>

<summary>Посмотреть</summary>

<img alt="screen_1.png" src=".img/screen_1.png" width="600"/>
<img alt="screen_2.png" src=".img/screen_2.png" width="600"/>
<img alt="screen_3.png" src=".img/screen_3.png" width="600"/>
<img alt="screen_4.png" src=".img/screen_4.png" width="600"/>

</details>

Тема интерфейса подстраивается под тему вашего браузера

### Основные функции
Реализованы только отмеченные
- [x] Учет сотрудников
- [x] Учет клиентов
- [x] Учет заявок от клиентов и какой сотрудник ведет заявку
- [x] Корзина для удаленных элементов с возможностью восстановления
- [x] Светофоры для обозначения статуса заказа
- [x] Роли админ / пользователь и форма входа 
- [ ] Дашборд со статистикой заявок
- [ ] Функция отправки оповещений по электронной почте при создании новой заявки
- [ ] Функция создания новой заявки с помощью письма электронной почты

### Модули
- Backend (Hibernate, Postgres)
- Frontend (Vaadin UI)

Взаимодействие модулей между собой осуществляется по gRPC (HTTP-2)

### Статусы светофоров
- ![Синий](https://img.shields.io/badge/-Синий-4a6fa5) Новая заявка (NEW)
- ![Зеленый](https://img.shields.io/badge/-Зеленый-4a8f5b) Пришло время начать выполнение (IN PROGRESS)
- ![Желтый](https://img.shields.io/badge/-Желтый-b8952e) Пришло время проконтролировать начальную стадию (FIRST CONTROL)
- ![Красный](https://img.shields.io/badge/-Красный-a94a4a) Пришло время проконтролировать завершающую стадию (SECOND CONTROL)
- ![Серый](https://img.shields.io/badge/-Серый-7a7a7a) Работа по заявке окончена (COMPLETED)

### Требования для запуска
Перед началом убедитесь, что установлено:

| Инструмент     | Версия                            | Проверка               |
|----------------|-----------------------------------|------------------------|
| Docker Desktop | 4.x+                              | docker --version       |
| Docker Compose | v2+                               | docker compose version |
| JDK            | 25                                | java -version          |
| Maven          | 3.9+                              | mvn -version           |
| Node.js        | 20+ (для локальной сборки Vaadin) | node -v                |
| Git            | любая                             | git --version          |

### Запуск приложения на локальной машине

Откройте терминал и выполните команды для запуска приложения.

**1. Склонировать проект**

```bash
git clone https://github.com/AleksOBM/vaadin-service-desk.git
```

**2. Перейти в корень проекта**

```bash
cd vaadin-service-desk
```

**3. Запуск**

Первый запуск будет долгим 5-15 мин.  
Необходим интернет для скачивания сторонних библиотек и образов.  

```bash
docker compose up --build -d
```

**4. Использование**

Откройте браузер на странице https://localhost:8080

**5. Остановка**

С сохранением данных

```bash
docker compose down
```

Без сохранения данных

```bash
docker compose down -v
```

**6. Повторный запуск**

```bash
docker compose up -d
```

**7. Полное удаление**

```bash
docker compose down -v --rmi all
```

### ⚠️ Предупреждение браузера о сертификате

При первом открытии страницы браузер покажет предупреждение **«Подключение не защищено»**.

**Это нормально для работы в локальной сети.**  
Проект использует самоподписанный сертификат, которому браузеры по умолчанию не доверяют.
При этом трафик всё равно шифруется.

**Что делать:** нажмите «Дополнительно» → «Перейти на localhost (небезопасно)».

Для работы в глобальной сети необходимо использовать сертификат от доверенного CA, тогда предупреждения не будет.

### REST эндпоинты фронтенда
```mermaid
mindmap
  root((Frontend))
    LoginView
      🌐/login
    AboutView
      🌐/about
    AgentsView
      🌐/agents
    OrdersView
      🌐/orders
    ClientsView
      🌐/clients
    RecycleView
      🌐/recycle
    SettingsView
      🌐/settings
    DashboardView
      🌐/dashboard
```

### gRPC эндпоинты бэкенда
```mermaid
%%{init: { 'mindmap': { 'maxNodeWidth': 500 } }}%%
mindmap
  root((gRPC API))
      🌐AdminOrderController
        deleteOrderPermanently
        restoreOrder
      🌐AdminClientController
        deleteClientPermanently
        restoreClient
        saveClient
        setClientDeleted
      🌐AdminAgentController
        deleteAgentPermanently
        restoreAgent
        saveAgent
        setAgentDeleted
      🌐AdminRecycleController
        findRecycle
        getFullRecycle
      🌐UserController
        saveOrder
        setOrderDeleted
        findAgents
        findClients
        findOrders
        getAllAgents
        getAllClients
        getAllOrders
      🌐FreeController
        login
```

### Схема базы данных

<img alt="db_map.png" src=".img/db_map.png" width="500"/>