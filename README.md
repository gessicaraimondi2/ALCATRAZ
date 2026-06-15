# 🏛️ ALCATRAZ – Database Web Application

A web application for managing a penitentiary facility, developed in Java with a MySQL database. The system supports two distinct user roles — visitors and administrators — providing differentiated interfaces for booking visits, managing inmates, staff, and rehabilitation activities.

---

## 🚀 Main Features

* 👤 **Authentication** for administrators and registered visitors
* 📅 **Visit booking** — visitors can request visits and track their outcome
* 👁️ **Visitor dashboard** — view aggregate information on inmates (sentence progress, courses attended, disciplinary measures)
* 🗂️ **Admin dashboard** — full CRUD access to inmates, cells, sections, staff, courses, enrollments, visits, bookings, and disciplinary measures
* 📊 **Statistics** — aggregate views on the inmate population and rehabilitation activity participation
* 🔒 **Role-based access control** — separate client interfaces with differentiated permissions

---

## 🧩 Technologies Used

* **Backend:** Java, JDBC
* **Frontend:** HTML5, CSS3, JavaScript (Fetch API)
* **Database:** MySQL
* **Build tool:** Gradle 8.7
* **Dependencies:** Unit 4.13.2

---

## 🏗️ Architecture

The application follows a layered architecture:

```
db_lab/
├── App.java                  # HTTP server entry point (port 8080)
├── Controller/               # HTTP handlers (one per entity)
│   ├── LoginController.java
│   ├── RegisterController.java
│   ├── DetenutoController.java
│   ├── PersonaleController.java
│   ├── PrenotazioneController.java
│   ├── VisitaController.java
│   ├── ProvvedimentoController.java
│   ├── CorsoController.java
│   ├── IscrizioneController.java
│   └── StatisticheController.java
├── data/                     # DAO layer + model classes
│   ├── Queries.java          # Centralized SQL queries
│   ├── DAOUtils.java         # JDBC connection and utilities
│   ├── DAO*.java             # One DAO per entity
│   └── *.java                # Entity model classes (with enums)
└── view/                     # Static frontend (served by the Java server)
    ├── Index.html
    ├── Login.html
    ├── Register.html
    ├── Dashboard-admin.html
    ├── Dashboard-visitatore.html
    ├── Prenotazione.html
    ├── Statistiche.html
    └── css/Style.css
```

---

## ⚙️ Installation & Configuration

### 1️⃣ Prerequisites

* Java 17+
* MySQL 
* Gradle 8.7 

### 2️⃣ Clone or Download the Project

```bash
git clone https://github.com/gessicaraimondi2/ALCATRAZ.git
```

### 3️⃣ Set Up the Database

1. Open MySQL Workbench
2. Create a new database named `alcatraz`
3. Import the SQL file included in the project to create the schema and populate initial data

### 4️⃣ Configure the Connection

In `App.java`, update the connection credentials if needed:

```java
connection = DAOUtils.localMySQLConnection("alcatraz", "root", "<your-password>");
```

### 5️⃣ Build and Run

```bash
./gradlew run
```

The server starts on **http://localhost:8080** and opens the browser automatically.

---

## 🔐 Access Credentials

The following accounts are pre-loaded with the SQL seed data:

**👑 Administrator**
* Email: `admin@alcatraz.it`
* Password: *(see SQL seed file)*

**👤 Visitor (test)**
* Register via the public registration page at `http://localhost:8080/Register.html`

---

## 👩‍💻 Authors

Gessica Raimondi, Valentina Severi — university project for the Databases.

---

## 📄 License

This project is distributed for educational purposes. You are free to modify and reuse it by citing the original source.
