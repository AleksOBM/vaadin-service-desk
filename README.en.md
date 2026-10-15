## Service Desk

![Static Badge](https://img.shields.io/badge/Java-25-green)
![Static Badge](https://img.shields.io/badge/Spring_Boot-4.1.1-green)
![Static Badge](https://img.shields.io/badge/Vaadin-25.2.8-blue)
![Static Badge](https://img.shields.io/badge/PostgreSQL-17-blue)
![Static Badge](https://img.shields.io/badge/Hibernate-4e7a62)
![Static Badge](https://img.shields.io/badge/Flyway-CC0202)
![Static Badge](https://img.shields.io/badge/Protobuf-blue)
![Static Badge](https://img.shields.io/badge/gRPC-4e7a62)
![Static Badge](https://img.shields.io/badge/Maven-orange)
![Static Badge](https://img.shields.io/badge/Docker-blue)
![Static Badge](https://img.shields.io/badge/Crossplatform-8512cb)

---

<span>
<a href="./README.md">Русский</a> | English
</span>

---

Distributed web application SPA + API for managing orders, clients, employees, as well as implementing three-stage deadline control.

### UI screenshots

<img alt="1.png" src=".img/1.png" width="600"/>
<img alt="1.1.png" src=".img/1.1.png" width="600"/>
<img alt="2.png" src=".img/2.png" width="600"/>
<img alt="3.png" src=".img/3.png" width="600"/>
<img alt="4.png" src=".img/4.png" width="600"/>
<img alt="5.png" src=".img/5.png" width="600"/>

### Main functions
Only the checked ones have been implemented
- [x] Employee management
- [x] Client management
- [x] Management of client orders and which employee is handling the order
- [x] Recycle bin for deleted items with the ability to restore
- [x] Traffic lights to indicate order status
- [ ] Admin / user roles and login form
- [ ] Dashboard with orders statistics
- [ ] Function for sending email notifications when a new order is created
- [ ] Function for creating a new order via email

### Modules
- Backend (Hibernate, Postgres)
- Frontend (Vaadin UI)

Modules interact with each other via gRPC (HTTP-2)

### Color-coded statuses
- ![Blue](https://img.shields.io/badge/-Blue-4a6fa5) New order request (NEW)
- ![Green](https://img.shields.io/badge/-Green-4a8f5b) It's time to start working on it (IN PROGRESS)
- ![Yellow](https://img.shields.io/badge/-Yellow-b8952e) It's time to check the initial stage (FIRST CONTROL)
- ![Red](https://img.shields.io/badge/-Red-a94a4a) It's time to check the final stage (SECOND CONTROL)
- ![Gray](https://img.shields.io/badge/-Gray-7a7a7a) The work on the request is finished (COMPLETED)

### Environmental Requirements
Before you begin, make sure the following is installed:

| Tool           | Version                              | Check                  |
|----------------|--------------------------------------|------------------------|
| Docker Desktop | 4.x+                                 | docker --version       |
| Docker Compose | v2+                                  | docker compose version |
| JDK            | 25                                   | java -version          |
| Maven          | 3.9+                                 | mvn -version           |
| Node.js        | 20+ (for local assembling of Vaadin) | node -v                |
| Git            | любая                                | git --version          |

### Running the Application on local host

Open a terminal and run the commands to start the application.

**1. Clone the project**

```bash
git clone https://github.com/AleksOBM/vaadin-service-desk.git
```

**2. Go to the project root**

```bash
cd vaadin-service-desk
```

**3. Launch**

The first launch will take a long time, 5-15 min.
An internet connection is required to download third-party libraries and images.

```bash
docker compose up --build -d
```

**4. Usage**

Open your browser at the page http://localhost:8080

**5. Stop**

Keeping the data

```bash
docker compose down
```

Without keeping the data

```bash
docker compose down -v
```

**6. Relaunch**

```bash
docker compose up -d
```

**7. Complete removal**

```bash
docker compose down -v --rmi all
```

### Frontend REST endpoints
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

### Backend gRPC endpoints
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

### Database map

<img alt="db_map.png" src=".img/db_map.png" width="500"/>