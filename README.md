## Service Desk

![Static Badge](https://img.shields.io/badge/Java-21-green)
![Static Badge](https://img.shields.io/badge/Spring_Boot-4.0.2-green)
![Static Badge](https://img.shields.io/badge/Vaadin-25.0.4-blue)
![Static Badge](https://img.shields.io/badge/Lombok-red)
![Static Badge](https://img.shields.io/badge/PostgreSQL-16.1-blue)
![Static Badge](https://img.shields.io/badge/Hibernate-4e7a62)
![Static Badge](https://img.shields.io/badge/JUnit-5-orange)
![Static Badge](https://img.shields.io/badge/H2_database-blue)
![Static Badge](https://img.shields.io/badge/Maven-orange)

Это приложение для работы с базой данных небольшой компании, занимающейся обслуживанием заявок клиентов.

![1](.img/1.png)
![1](.img/2.png)

### Основные функции
- [x] Учет сотрудников
- [x] Учет клиентов
- [x] Учет заявок от клиентов и какой сотрудник ведет заявку
- [x] Корзина для удаленных элементов с возможностью восстановления
- [ ] Дашборд со статистикой заявок
- [ ] Функция отправки оповещений по электронной почте при создании новой заявки

### Требования (Environmental Requirements)
Для запуска проекта требуется: - Java версии 21  
Приложение работает на порту указанном в файле application.yaml  
Для работы с приложением используйте любой браузер или веб-клиент

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

### Схема базы данных (Database map)
![DatabaseMap](database-map.png "Database map:")

#### Архитектурный стиль (Architectural Style)
- Монолитная архитектура (monolithic)  
- Слоистая структура (layered architecture)
- Серверная генерация UI (server-side rendering)
- Типобезопасный UI (strongly typed UI)
