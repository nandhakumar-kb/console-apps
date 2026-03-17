# Library Management System (Java Console App)

A simple and beginner-friendly monolith console application built using Java.

## Project Structure

- `src/` -> Java source code
- `bytecode/` -> compiled `.class` files

## Features

### Module A: Authentication and Welcome Menu
- Login for Admin and Borrower using email and password
- Role-based menu after successful login

### Module B: Book Inventory Management (Admin)
- Add book
- Modify book details (name, author, quantity, price)
- Delete book
- View books sorted by name or available quantity
- Search book by name or ISBN
- Add Admin and Borrower users
- Manage borrower security deposit

### Module C: Borrowing Book (Borrower)
- View available books
- Add/remove books in checkout cart
- Checkout books from cart
- Maximum 3 books at a time
- Cannot borrow same book twice
- Minimum security deposit required: Rs 500

### Module D: Fine and Regulations
- Initial borrower security deposit: Rs 1500
- Late return fine:
  - First 10 overdue days: Rs 2/day
  - Fine doubles every next 10-day block
  - Maximum fine capped at 80% of book cost
- Lost book fine: 50% of book cost
- Membership card lost fine: Rs 10
- Fine payment modes:
  - Cash
  - Deduct from security deposit
- Borrower options per transaction:
  - Extend tenure (max 2 times)
  - Exchange book
  - Mark book as lost

### Module E: Reports
Admin can view:
- Low stock books
- Never borrowed books
- Heavily borrowed books
- Outstanding books as on a given date
- Active status of book by ISBN and borrower details

Borrower can view:
- Fine history
- Borrow history

## Default Login Credentials

### Admin
- Email: admin@library.com
- Password: admin123

### Borrower
- Email: sathya@student.com
- Password: sathya123

## Compile and Run

Run these commands from project root:

```bash
javac -d bytecode -sourcepath src src/library/Main.java
java -cp bytecode library.Main
```

