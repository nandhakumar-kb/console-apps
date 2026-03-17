# Super Market Billing System (Java Console App)

A simple and beginner-friendly monolith console application for a supermarket built using Java.

## Project Structure

- `src/` -> Java source code
- `bytecode/` -> compiled `.class` files

## Features

### Module A: Authentication and Welcome Menu
- Login for Admin and Customer using email and password
- Role-based menu after successful login

### Module B: Inventory Management (Admin)
- Add product
- Modify product details (name, price, quantity)
- Delete product
- View products sorted by name or price
- Search product by name
- Add Admin and Customer users
- Increase customer credit balance

### Module C: Customer Purchase
- View list of available products
- Select products by Product ID
- Specify quantity
- Add/update/remove items from cart
- Multiple quantities of same product allowed
- Proceed to checkout

### Module D: Payment and Rewards
- Initial customer credit: Rs 1000
- Can only purchase within credit limit
- Reward system:
  - **Option A:** If bill >= Rs 5000: Add Rs 100 to wallet (no loyalty points)
  - **Option B:** For every Rs 100 spent: 1 Loyalty Point
    - 50 Loyalty Points = Rs 100 discount on next bill
- Payment deducted from credit

### Module E: Purchase History
- View all bills with date and amount
- View detailed bill with itemized breakdown

### Module F: Reports (Admin)
- Low stock products (refill alert)
- Never sold products
- Top customers by spending value
- Top selling admins
- Top sold products

## Default Login Credentials

### Admin
- Email: admin@supermarket.com
- Password: admin123

### Customer
- Email: sathya@customer.com
- Password: sathya123

## Compile and Run

Run these commands from project root:

```bash
javac -d bytecode -sourcepath src src/supermarket/Main.java
java -cp bytecode supermarket.Main
```

