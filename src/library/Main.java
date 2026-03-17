package library;

import java.util.Scanner;
import library.menu.AdminMenu;
import library.menu.BorrowerMenu;
import library.model.Admin;
import library.model.Borrower;
import library.model.User;
import library.service.AuthService;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();

        System.out.println("===========================================");
        System.out.println("    LIBRARY MANAGEMENT SYSTEM             ");
        System.out.println("===========================================");
        System.out.println("  Default Admin   : admin@library.com / admin123");
        System.out.println("  Sample Borrower : sathya@student.com  / sathya123");
        System.out.println("===========================================");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose: ");
            String input = sc.nextLine().trim();

            if (input.equals("2")) {
                System.out.println("Thank you for using the Library System. Goodbye!");
                break;
            }
            if (!input.equals("1")) {
                System.out.println("Please enter 1 or 2.");
                continue;
            }

            System.out.print("Email    : ");
            String email = sc.nextLine().trim();
            System.out.print("Password : ");
            String password = sc.nextLine().trim();

            User user = authService.login(email, password);
            if (user == null) {
                System.out.println("Invalid email or password. Please try again.");
                continue;
            }

            System.out.println("\nLogin successful! Welcome, " + user.getName()
                    + "  [" + user.getRole() + "]");

            if (user.getRole().equals("ADMIN")) {
                new AdminMenu(sc, (Admin) user).show();
            } else {
                new BorrowerMenu(sc, (Borrower) user).show();
            }
        }
    }
}

/*compile command: javac -d bytecode -sourcepath src src/library/Main.java
run command: java -cp bytecode library.Main */