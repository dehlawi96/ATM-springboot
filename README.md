# ATM System — Spring Boot + Java + HTML/CSS/JS

A complete ATM simulation system built using **Spring Boot**, **Java**, and a simple **HTML/CSS/JS frontend**.  
This project supports card insertion, PIN authentication, checking balance, deposit, withdrawal, transaction history, and logout.

---

## 🚀 Features

### ✔ Authentication  
- Card insertion simulation  
- PIN validation  
- Generates session token  
- Logout endpoint  

### ✔ Banking Operations  
- Check balance  
- Deposit money  
- Withdraw with validation  
- View transaction history  

### ✔ Clean UI  
- Fully implemented in HTML/CSS/JavaScript  
- Calls backend through REST APIs  
- Smooth action panels (Deposit/Withdraw/History)

---

## 🛠️ Tech Stack

### **Backend**
- Java 17+
- Spring Boot
- Maven
- REST Controllers

### **Frontend**
- HTML  
- CSS  
- JavaScript (Fetch API)

---

## 📁 Project Structure

src/
└── main/
├── java/com/example/atm
│ ├── AtmApplication.java
│ ├── controller/
│ ├── service/
│ ├── model/
│ └── repository/
└── resources/
├── static/
│ ├── index.html
│ ├── script.js
│ └── style.css
└── application.properties


---

## 🔥 API Endpoints

### **Auth**
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login using cardNumber + PIN |
| POST | `/api/auth/logout` | Logout |

### **Account**
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/account/balance` | Get account balance |
| POST | `/api/account/deposit` | Deposit money |
| POST | `/api/account/withdraw` | Withdraw money |
| GET | `/api/account/transactions` | Get transaction list |

---

## ▶️ How to Run

### **Method 1: Using IntelliJ**
1. Open project folder  
2. Let Maven import dependencies  
3. Run `AtmApplication.java`

### **Method 2: Using Terminal**
mvn spring-boot:run
Backend will start on: **http://localhost:8080**

Frontend is automatically served from:  
src/main/resources/static/index.html

---

## 📄 License
This project is licensed under the **MIT License** (see LICENSE file).

---

## ⭐ Support
If you like this project, consider giving it a **⭐ star** on GitHub!
