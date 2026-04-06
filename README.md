# Java Console Applications

A single beginner-friendly Java console project with one launcher and four modules.

---

## Project Structure

```
console-apps-main/
+-- src/
|   +-- app/          -> common launcher
|   +-- library/      -> Library Management System
|   +-- supermarket/  -> Super Market Billing System
|   +-- atm/          -> ATM Simulation
|   +-- vehiclerental/-> Vehicle Rental System
+-- bytecode/         -> compiled .class files
+-- README.md
```

---

## Launcher

Run one menu and choose any module:

1. Library Management System
2. Super Market Billing System
3. ATM Simulation
4. Vehicle Rental System
5. Exit

### Compile and Run Launcher

```bash
javac -d bytecode -sourcepath src src/app/Main.java
java -cp bytecode app.Main
```

---

## 1. Library Management System

A console-based library management system for Borrowers and Administrators.

### Modules

**Module A: Authentication and Welcome Menu**
- Login for Admin and Borrower using email and password
- Role-based menu after successful login

**Module B: Book Inventory Management (Admin)**
- Add, modify, delete books
- View books sorted by name or available quantity
- Search book by name or ISBN
- Add Admin and Borrower users
- Manage borrower security deposit

**Module C: Borrowing Book (Borrower)**
- View available books
- Add/remove books in checkout cart
- Maximum 3 books at a time
- Cannot borrow same book twice
- Minimum security deposit required: Rs 500

**Module D: Fine and Regulations**
- Initial borrower security deposit: Rs 1500
- Late return fine: Rs 2/day, doubles every 10-day block, capped at 80% of book cost
- Lost book fine: 50% of book cost
- Membership card lost fine: Rs 10
- Payment via cash or security deposit deduction
- Extend tenure (max 2 times), exchange book, mark book as lost

**Module E: Reports (Admin)**
- Low stock books, never borrowed books, heavily borrowed books
- Outstanding books as on a given date
- Active book status by ISBN with borrower details

**Reports (Borrower)**
- Fine history and borrow history

### Default Login Credentials

| Role     | Email               | Password |
|----------|---------------------|----------|
| Admin    | admin@library.com   | admin123 |
| Borrower | sathya@student.com   | sathya123 |

### Compile and Run

```bash
javac -d bytecode -sourcepath src src/library/Main.java
java -cp bytecode library.Main
```

---

## 2. Super Market Billing System

A console-based supermarket billing system for Customers and Administrators.

### Modules

**Module A: Authentication and Welcome Menu**
- Login for Admin and Customer using email and password
- Role-based menu after successful login

**Module B: Inventory Management (Admin)**
- Add, modify, delete products
- View products sorted by name or price
- Search product by name
- Add Admin and Customer users
- Increase customer credit balance

**Module C: Customer Purchase**
- View available products
- Add products to cart by Product ID and quantity
- Add same product multiple times (quantity merges)
- Edit quantities or remove items from cart
- Proceed to checkout

**Module D: Payment and Rewards**
- Initial customer credit: Rs 1000
- Purchase only within credit limit
- Reward system:
  - Bill >= Rs 5000: Rs 100 cashback to wallet (no loyalty points)
  - Every Rs 100 spent: 1 Loyalty Point; 50 points = Rs 100 discount on next bill

**Module E: Purchase History**
- View all bills with date and amount
- View detailed itemized bill by bill number

**Module F: Reports (Admin)**
- Low stock products, never sold products
- Top customers by spending value
- Top selling admins
- Top sold products

### Default Login Credentials

| Role     | Email                   | Password |
|----------|-------------------------|----------|
| Admin    | admin@supermarket.com   | admin123 |
| Customer | sathya@customer.com       | sathya123  |

### Compile and Run

```bash
javac -d bytecode -sourcepath src src/supermarket/Main.java
java -cp bytecode supermarket.Main
```

---

## 3. ATM Simulation

A console-based ATM simulation with basic banking operations.

### Features

- PIN login with 3 attempts
- Check account balance
- Deposit money
- Withdraw money
- Exit

### Default PIN

- 1234

### Compile and Run

```bash
javac -d bytecode -sourcepath src src/atm/Main.java
java -cp bytecode atm.Main
```

---

## 4. Vehicle Rental System

A monolith console-based vehicle rental system for Borrowers and Administrators.

### Modules

**Module A: Sign In and Sign Up**
- Login for Admin and Borrower using email and password
- Signup support for both roles
- Role-based welcome menus after authentication

**Module B: Vehicle Inventory (Admin)**
- Add, modify, and delete vehicles (Car/Bike)
- View all vehicles sorted by name or available count
- Search vehicles by name or number plate
- Change borrower security deposit amount
- Mark vehicles as serviced to re-enable catalog listing

**Module C: Renting a Vehicle (Borrower)**
- View available catalog of cars and bikes
- Add/remove vehicles in checkout cart
- Borrower can rent only one car and one bike at a time
- Checkout validates minimum deposit:
  - Bike: Rs 3000
  - Car: Rs 10000

**Module D: Fine and Regulations**
- Initial caution deposit per borrower: Rs 30000
- Vehicle rented for same-day return by default
- Additional 15% fine if usage exceeds 500 km/day
- Car damage labels and fine multipliers:
  - LOW: 20%
  - MEDIUM: 50%
  - HIGH: 75%
- Service regulation:
  - Car service due at 3000 km
  - Bike service due at 1500 km
  - Service-due vehicles are hidden from catalog
- Fine payment mode:
  - Cash
  - Deduct from security deposit
- Transaction actions supported per rental:
  - Extend tenure (max 2 consecutive extensions)
  - Exchange vehicle
  - Mark vehicle as lost
  - Return vehicle

**Module E: Reports**

Admin can view:
- Vehicles due for service
- All vehicles sorted by rental price
- Search by name and filter by Car/Bike
- Rented-out vehicles
- Never-rented vehicles

Borrowers can view:
- Previous rentals
- Fine history

### Default Login Credentials

| Role     | Email            | Password |
|----------|------------------|----------|
| Admin    | admin@rental.com | admin123 |
| Borrower | kiran@user.com   | kiran123 |

### Compile and Run

```bash
javac -d bytecode -sourcepath src src/vehiclerental/Main.java
java -cp bytecode vehiclerental.Main
```

---

## Notes

- All four projects use in-memory data storage (no database)
- Data resets each time the application restarts
- Designed to be simple and easy to understand for beginners
