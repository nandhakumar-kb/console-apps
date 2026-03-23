package atm;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double balance = 1000.0;
        int pin = 1234;
        int attempts = 0;
        boolean loggedIn = false;

        System.out.println("===== ATM Simulation =====");

        while (attempts < 3) {
            System.out.print("Enter 4-digit PIN: ");
            int enteredPin = sc.nextInt();
            if (enteredPin == pin) {
                loggedIn = true;
                break;
            } else {
                attempts++;
                System.out.println("Wrong PIN. Attempts left: " + (3 - attempts));
            }
        }

        if (!loggedIn) {
            System.out.println("Too many wrong attempts. Account locked.");
            sc.close();
            return;
        }

        int choice;
        do {
            System.out.println("\n1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            if (choice == 1) {
                System.out.println("Current balance: Rs." + balance);
            } else if (choice == 2) {
                System.out.print("Enter deposit amount: ");
                double amount = sc.nextDouble();
                if (amount > 0) {
                    balance = balance + amount;
                    System.out.println("Deposit successful. New balance: Rs." + balance);
                } else {
                    System.out.println("Invalid amount.");
                }
            } else if (choice == 3) {
                System.out.print("Enter withdraw amount: ");
                double amount = sc.nextDouble();
                if (amount <= 0) {
                    System.out.println("Invalid amount.");
                } else if (amount > balance) {
                    System.out.println("Insufficient balance.");
                } else {
                    balance = balance - amount;
                    System.out.println("Withdrawal successful. New balance: Rs." + balance);
                }
            } else if (choice == 4) {
                System.out.println("Thank you for using ATM.");
            } else {
                System.out.println("Invalid choice.");
            }
        } while (choice != 4);

        sc.close();
    }
}
