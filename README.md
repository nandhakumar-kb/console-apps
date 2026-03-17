# Java Console Applications

A collection of simple, beginner-friendly monolith console applications built using Java.

---

## 1. Library Management System

A console-based library management system for Borrowers and Administrators.

### Project Structure

```
LibraryManagementSystem/
+-- src/         -> Java source code
+-- bytecode/    -> compiled .class files
```

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
cd LibraryManagementSystem
javac -d bytecode -sourcepath src src/library/Main.java
java -cp bytecode library.Main
```

---

## 2. Super Market Billing System

A console-based supermarket billing system for Customers and Administrators.

### Project Structure

```
SuperMarketBillingSystem/
+-- src/         -> Java source code
+-- bytecode/    -> compiled .class files
```

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
cd SuperMarketBillingSystem
javac -d bytecode -sourcepath src src/supermarket/Main.java
java -cp bytecode supermarket.Main
```

---

## Notes

- Both projects use in-memory data storage (no database)
- Data resets each time the application restarts
- Designed to be simple and easy to understand for beginners
