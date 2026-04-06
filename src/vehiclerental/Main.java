package vehiclerental;

import java.util.Scanner;
import vehiclerental.menu.AdminMenu;
import vehiclerental.menu.BorrowerMenu;
import vehiclerental.model.Admin;
import vehiclerental.model.Borrower;
import vehiclerental.model.User;
import vehiclerental.service.AuthService;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();

        System.out.println("===========================================");
        System.out.println("       VEHICLE RENTAL SYSTEM               ");
        System.out.println("===========================================");
        System.out.println("  Default Admin    : admin@rental.com / admin123");
        System.out.println("  Sample Borrower  : kiran@user.com / kiran123");
        System.out.println("===========================================");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            if ("3".equals(choice)) {
                System.out.println("Thank you for using Vehicle Rental System.");
                return;
            }

            if ("2".equals(choice)) {
                signup(scanner, authService);
                continue;
            }

            if (!"1".equals(choice)) {
                System.out.println("Please choose 1, 2 or 3.");
                continue;
            }

            System.out.print("Email    : ");
            String email = scanner.nextLine().trim();
            System.out.print("Password : ");
            String password = scanner.nextLine().trim();

            User user = authService.login(email, password);
            if (user == null) {
                System.out.println("Invalid email or password.");
                continue;
            }

            System.out.println("\nWelcome, " + user.getName() + " [" + user.getRole() + "]");
            if ("ADMIN".equals(user.getRole())) {
                new AdminMenu(scanner, (Admin) user).show();
            } else {
                new BorrowerMenu(scanner, (Borrower) user).show();
            }
        }
    }

    private static void signup(Scanner scanner, AuthService authService) {
        System.out.println("Signup role: 1. Borrower  2. Admin");
        System.out.print("Choose: ");
        String roleInput = scanner.nextLine().trim();
        String role = "2".equals(roleInput) ? "ADMIN" : "BORROWER";

        System.out.print("Email    : ");
        String email = scanner.nextLine().trim();
        System.out.print("Password : ");
        String password = scanner.nextLine().trim();
        System.out.print("Name     : ");
        String name = scanner.nextLine().trim();

        System.out.println(authService.signup(role, email, password, name));
    }
}

/*compile command: javac -d bytecode -sourcepath src src/vehiclerental/Main.java
run command: java -cp bytecode vehiclerental.Main */
