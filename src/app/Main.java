package app;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("===========================================");
            System.out.println("      JAVA CONSOLE APPS LAUNCHER");
            System.out.println("===========================================");
            System.out.println("1. Library Management System");
            System.out.println("2. Super Market Billing System");
            System.out.println("3. ATM Simulation");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            String input = sc.nextLine().trim();

            if (input.equals("1")) {
                library.Main.main(new String[0]);
            } else if (input.equals("2")) {
                supermarket.Main.main(new String[0]);
            } else if (input.equals("3")) {
                atm.Main.main(new String[0]);
            } else if (input.equals("4")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Please enter 1, 2, 3, or 4.");
            }

            System.out.println();
        }
    }
}
