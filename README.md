# 🎯 Camerounian Tontine Management System

**A Camerounian tontine** is a traditional financial system where members meet—usually once a week—to contribute a fixed amount of money. This amount, agreed upon at the beginning of each cycle, is collected and given to one member who uses it to fund a personal project. The following week, another member receives the contribution, and the rotation continues until everyone has received their share.
A tontine functions like an association with an elected or appointed executive committee responsible for ensuring smooth organization and transparency. Each meeting begins with a review of the previous session’s events, followed by the agenda for the current meeting. At the end, once the beneficiary (or beneficiaries) has received the collected funds, the members share a meal and drinks as part of a communal celebration.
**This application aims to digitize the entire tontine process**, from the creation of the group to the management of meetings, contributions, beneficiaries, and reports, ensuring a modern, transparent, and efficient experience for all participants.

---

## 🚀 Features

### 🔹 Tontine Creation & Management
- Create and configure tontines with:
  - Name, objectives, meeting frequency
  - Contribution amount
  - Duration of cycles
- Add and manage members
- Assign roles (President, Treasurer, Secretary, etc.)

### 🔹 Member & Contribution Tracking
- Register weekly contributions
- Track payment status for each member
- Handle late or missing payments
- Automatic calculation of total collected amount

### 🔹 Meeting Management
- Create and log meeting sessions
- Record:
  - Previous meeting summary
  - Agenda items
  - Attendance
  - Decisions taken
- Track beneficiaries for each rotation

### 🔹 Beneficiary Rotation
- Automatic or manual selection of the weekly beneficiary
- Register received amount
- Track who has already benefited

### 🔹 Reporting & Transparency
- Financial dashboards
- Session history
- Contribution history per member
- Export reports (PDF / Excel)

### 🔹 Social & Community Aspect
- Record post-meeting celebrations (food, drinks, event notes)
- Keep the cultural spirit of the tontine alive, even in digital form

---

## 🏛️ Architecture (Microservices)

The project follows a modern microservice-based architecture:

- **Gateway Service** — entry point for all requests    
- **Config Service** — centralized configuration  
- **Customer Service** — members, roles & authentication  
- **Tontine Service** — tontine creation, structure & management  
- **Session Service** — meetings, agendas, summaries  
- **Contribution Service** — payments, rotations, beneficiary logic  
- **Notification Service** — reminders & updates (email/SMS)  
- **Common Libraries** — shared DTOs, utilities & exceptions  


                             +------------------------------+
                             |        API Gateway           |
                             | (Routing, Auth, Rate Limit)  |
                             +--------------+---------------+
                                            |
                                            v
                 -----------------------------------------------------------
                 |                 Kubernetes Cluster                      |
                 -----------------------------------------------------------
                   |                   |                |                |
                   v                   v                v                v

     +---------------------+  +----------------+  +----------------+  +----------------+
     |  Customer Service   |  |  Tontine       |  |   Session      |  | Contribution   |
     |---------------------|  |   Service      |  |   Service      |  |   Service      |
     | + Manage customers  |  |----------------|  |----------------|  |----------------|
     | + Roles & profiles  |  | + Create       |  | + Agenda mgmt  |  | + Register     |
     | + Authentication    |  |   tontines     |  | + Attendance   |  |   payments     |
     |   (JWT/Keycloak)    |  | + Members      |  | + Summary      |  | + Rotation     |
     +----------+----------+  | + Bureau       |  |   history      |  |   logic        |
                |             +--------+-------+  +--------+-------+  +--------+-------+
                |                      |                   |                   |
                |                      v                   |                   v
                |            +----------------+            |         +----------------+
                |            | Beneficiary    |            |         | Notification   |
                |            |   Service      |            |         |   Service      |
                |            |----------------|            |         |----------------|
                |            | + Select next  |            |         | + Emails       |
                |            |   beneficiary  |            |         | + SMS          |
                |            +--------+-------+            |         +--------+-------+
                |                     |                    |                  |
                |                     |                    |                  |
                |---------------------|--------------------|------------------|
                                      |
                                      v
                           +------------------------+
                           |      Event Bus         |
                           |  (Kafka / RabbitMQ)    |
                           +------------------------+

---

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot / Spring Cloud**
- **PostgreSQL or MySQL**
- **Docker & Docker Compose**
- **Kafka or RabbitMQ** (optional, for events)
- **OpenAPI / Swagger**
- **Keycloak or JWT** for authentication

---

## 📦 Installation & Setup

### 1️⃣ Clone the repository

```bash
git https://github.com/Mubson123/tontine-microservices.git
cd tontine-app
