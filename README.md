# 📊 System Monitor – Diploma Project
 **An educational project to explore Spring Boot, Hibernate, databases, and full-stack development.**

## 📝 About the Project

This project was made quickly and is not intended to be a large-scale system.
As an educational project, it helped me study:

- Java Spring Boot

- Hibernate ORM

- Database design and interaction

- Frontend development with React

- Agent-based data collection

 ## ❗ 
 ### The main focus is on the backend, which handles all server processes, database operations, and user authentication.

The project consists of three parts:

 1) Backend (Java Spring Boot + Gradle):
 - Handles all business logic

- Connects to the database

- Manages user registration, login, and authorization

- Receives and stores metrics from agents

- Tracks agent status (online/offline)

2) Frontend (React + Vite + JavaScript):

- Displays graphs and metrics

- Allows user registration and login

- Shows agent statuses

3)Agent (Python):

- Collects metrics locally

- Sends metrics to the server periodically

## ⚡ Features

- Set thresholds for metrics with sound alerts when exceeded

- Change the metrics reporting interval per agent

- View agent status (online/offline)

- Visualize graphs and historical data

## 🚀 Installation & Running
### 1️⃣ Clone the repository
```
git clone link
cd system-monitor-java
```

### 2️⃣ Run the project via Docker Compose

The backend depends on Docker for the database. The easiest way to start everything is:

```
docker-compose up -d --build
```

This command will automatically start:

- Backend (Spring Boot server)

- Database (PostgreSQL/MySQL, depending on config)

You do not need to run ./gradlew bootRun manually unless you want to run the backend locally without Docker (not recommended without configuring a database).

### 3️⃣ Frontend
```
cd System-Monitor-FrontEnd
npm install
npm run dev
```

Open your browser at http://localhost:5173 (default Vite port) to view the dashboard.

### 4️⃣ Python Agent
```
cd System-Monitor-agent
pip install -r requirements.txt
python agent.py
```

The agent will start sending metrics to the server according to its configuration.

### ️5️⃣ Using
Then register on site using login and password and login in Python Agent in setting.py. 
you can also change the name of agent.

## 🛠 Technologies

Backend: Java, Spring Boot, Hibernate, Gradle, MySQL, Flyway.

Frontend: React, Vite, JavaScript

Agent: Python

Additional: Docker, Docker Compose, JUnit, Mockito

## 📚 Learning Outcomes

Working on this project allowed me to explore:

- Building REST APIs with Spring Boot

- Database interactions using Hibernate

- User authentication and authorization

- Creating a simple frontend with React

- Handling agent data collection and reporting