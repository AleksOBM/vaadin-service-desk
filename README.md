## Service Desk

![Static Badge](https://img.shields.io/badge/Java-25-green)
![Static Badge](https://img.shields.io/badge/Spring_Boot-4.1.1-green)
![Static Badge](https://img.shields.io/badge/Vaadin-25.2.8-blue)
![Static Badge](https://img.shields.io/badge/PostgreSQL-17-blue)
![Static Badge](https://img.shields.io/badge/Hibernate-4e7a62)
![Static Badge](https://img.shields.io/badge/Protobuf-blue)
![Static Badge](https://img.shields.io/badge/gRPC-4e7a62)
![Static Badge](https://img.shields.io/badge/Maven-orange)
![Static Badge](https://img.shields.io/badge/Docker-blue)
![Static Badge](https://img.shields.io/badge/Microservice-red)
![Static Badge](https://img.shields.io/badge/Crossplatform-8512cb)

Это приложение для работы с базой данных небольшой компании, занимающейся обслуживанием заявок клиентов.

<img alt="1.png" src=".img/1.png" width="600"/>
<img alt="1.1.png" src=".img/1.1.png" width="600"/>
<img alt="2.png" src=".img/2.png" width="600"/>
<img alt="3.png" src=".img/3.png" width="600"/>
<img alt="4.png" src=".img/4.png" width="600"/>
<img alt="5.png" src=".img/5.png" width="600"/>

### Основные функции
- [x] Учет сотрудников
- [x] Учет клиентов
- [x] Учет заявок от клиентов и какой сотрудник ведет заявку
- [x] Корзина для удаленных элементов с возможностью восстановления
- [ ] Дашборд со статистикой заявок
- [ ] Функция отправки оповещений по электронной почте при создании новой заявки

### Модули (Modules)
- Backend (Hibernate, Postgres)
- Frontend (Vaadin UI)

Взаимодействие модулей между собой осуществляется по gRPC (HTTP-2)

### Требования для запуска (Environmental Requirements)
Перед началом убедитесь, что установлено:

| Инструмент     | Версия                            | Проверка               |
|----------------|-----------------------------------|------------------------|
| Docker Desktop | 4.x+                              | docker --version       |
| Docker Compose | v2+                               | docker compose version |
| JDK            | 25                                | java -version          |
| Maven          | 3.9+                              | mvn -version           |
| Node.js        | 20+ (для локальной сборки Vaadin) | node -v                |
| Git            | любая                             | git --version          |

### Запуск приложения на локальной машине (Running the Application)

Откройте терминал и выполните команды для запуска приложения.

**1. Склонировать проект**

```bash
git clone <URL_репозитория>
```

**2. Перейти в корень проекта**

```bash
cd <имя_проекта>
```

**3. Запуск**

Первый запуск будет долгим 5-15 мин.  
Необходим интернет для скачивания сторонних библиотек и образов.  

```bash
docker compose up --build -d
```

**4. Использование**

Откройте браузер на странице http://localhost:8080

**5. Остановка**

Без сохранения данных

```bash
docker compose down -v
```

С сохранением данных

```bash
docker compose down
```

**6. Полное удаление**

```bash
docker compose down -v --rmi all
```

### REST эндпоинты фронтенда
```mermaid
mindmap
  root((Frontend))
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

### gRPC эндпоинты бэкенда (Backend gRPC endpoints)
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
      🌐FreeController
        findAgents
        findClients
        findOrders
        getAllAgents
        getAllClients
        getAllOrders
```

### Схема базы данных (Database map)

<img alt="db_map.png" src=".img/db_map.png" width="500"/>