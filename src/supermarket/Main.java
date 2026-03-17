package supermarket;

import java.util.Scanner;
import supermarket.menu.AdminMenu;
import supermarket.menu.CustomerMenu;
import supermarket.model.Admin;
import supermarket.model.Customer;
import supermarket.model.User;
import supermarket.service.AuthService;

public class Main {

    public static void main(String[] args) {
        Scanner     scanner     = new Scanner(System.in);
        AuthService authService = new AuthService();

        System.out.println("===========================================");
        System.out.println("  SUPER MARKET BILLING SYSTEM              ");
        System.out.println("===========================================");
        System.out.println("  Admin    : admin@supermarket.com / admin123");
        System.out.println("  Customer : sathya@customer.com / sathya123");
        System.out.println("===========================================");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose: ");
            String input = scanner.nextLine().trim();

            if (input.equals("2")) {
                System.out.println("Thank you for shopping. Goodbye!");
                break;
            }
            if (!input.equals("1")) {
                System.out.println("Please enter 1 or 2.");
                continue;
            }

            System.out.print("Email    : ");
            String email = scanner.nextLine().trim();
            System.out.print("Password : ");
            String password = scanner.nextLine().trim();

            User user = authService.login(email, password);
            if (user == null) {
                System.out.println("Invalid email or password. Please try again.");
                continue;
            }

            System.out.println("\nLogin successful! Welcome, " + user.getName()
                    + "  [" + user.getRole() + "]");

            if (user.getRole().equals("ADMIN")) {
                new AdminMenu(scanner, (Admin) user).show();
            } else {
                new CustomerMenu(scanner, (Customer) user).show();
            }
        }
    }
}
/*compile command: javac -d bytecode -sourcepath src src/supermarket/Main.java
run command: java -cp bytecode supermarket.Main */
