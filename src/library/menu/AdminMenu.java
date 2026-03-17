package library.menu;

import library.data.DataStore;
import library.model.*;
import library.service.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private final Scanner   scanner;
    private final Admin         admin;
    private final BookService   bookService   = new BookService();
    private final ReportService reportService = new ReportService();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AdminMenu(Scanner scanner, Admin admin) {
        this.scanner = scanner;
        this.admin   = admin;
    }

    public void show() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  ADMIN MENU  |  Welcome, " + admin.getName());
            System.out.println("========================================");
            System.out.println("1. Book Management");
            System.out.println("2. User Management");
            System.out.println("3. Reports");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: bookManagement(); break;
                case 2: userManagement(); break;
                case 3: reports();        break;
                case 4:
                    System.out.println("Logged out. Goodbye, " + admin.getName() + "!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void bookManagement() {
        while (true) {
            System.out.println("\n--- Book Management ---");
            System.out.println("1. Add Book");
            System.out.println("2. Modify Book");
            System.out.println("3. Delete Book");
            System.out.println("4. View All Books");
            System.out.println("5. Search Book");
            System.out.println("6. Back");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: addBook();    break;
                case 2: modifyBook(); break;
                case 3: deleteBook(); break;
                case 4: viewAllBooks(); break;
                case 5: searchBook(); break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addBook() {
        System.out.print("ISBN        : "); String isbn   = scanner.nextLine().trim();
        System.out.print("Book Name   : "); String name   = scanner.nextLine().trim();
        System.out.print("Author      : "); String author = scanner.nextLine().trim();
        System.out.print("Quantity    : "); int    qty    = readInt();
        System.out.print("Price (Rs)  : "); double price  = readDouble();
        if (bookService.addBook(isbn, name, author, qty, price)) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("A book with ISBN " + isbn + " already exists.");
        }
    }

    private void modifyBook() {
        System.out.print("Enter ISBN of book to modify: ");
        String isbn = scanner.nextLine().trim();
        Book book = DataStore.findBookByIsbn(isbn);
        if (book == null) { System.out.println("Book not found."); return; }
        System.out.println("Current: " + book);
        System.out.println("Modify: 1.Name  2.Author  3.Price  4.Available Qty  5.Total Qty  6.Back");
        System.out.print("Choose: ");
        int choice = readInt();
        switch (choice) {
            case 1:
                System.out.print("New Name: ");
                book.setName(scanner.nextLine().trim());
                System.out.println("Name updated.");
                break;
            case 2:
                System.out.print("New Author: ");
                book.setAuthor(scanner.nextLine().trim());
                System.out.println("Author updated.");
                break;
            case 3:
                System.out.print("New Price (Rs): ");
                book.setPrice(readDouble());
                System.out.println("Price updated.");
                break;
            case 4:
                System.out.print("New Available Quantity: ");
                book.setAvailableQuantity(readInt());
                System.out.println("Available quantity updated.");
                break;
            case 5:
                System.out.print("New Total Quantity: ");
                book.setTotalQuantity(readInt());
                System.out.println("Total quantity updated.");
                break;
            case 6: return;
            default: System.out.println("Invalid choice.");
        }
    }

    private void deleteBook() {
        System.out.print("Enter ISBN of book to delete: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Confirm delete? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            if (bookService.deleteBook(isbn)) {
                System.out.println("Book deleted.");
            } else {
                System.out.println("Book not found.");
            }
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private void viewAllBooks() {
        System.out.println("Sort by: 1. Name   2. Available Quantity");
        System.out.print("Choose: ");
        int choice = readInt();
        List<Book> books = (choice == 2)
                ? bookService.getAllBooksSortedByAvailableQty()
                : bookService.getAllBooksSortedByName();
        printBookTable(books);
    }

    private void searchBook() {
        System.out.println("Search by: 1. Name   2. ISBN");
        System.out.print("Choose: ");
        int choice = readInt();
        if (choice == 1) {
            System.out.print("Enter name (partial match ok): ");
            List<Book> results = bookService.searchByName(scanner.nextLine().trim());
            if (results.isEmpty()) { System.out.println("No matching books found."); return; }
            printBookTable(results);
        } else {
            System.out.print("Enter ISBN: ");
            Book b = bookService.searchByIsbn(scanner.nextLine().trim());
            if (b == null) System.out.println("Book not found.");
            else           System.out.println(b);
        }
    }

    private void printBookTable(List<Book> books) {
        System.out.println("\n" + String.format("%-10s %-35s %-22s %9s %6s %10s",
                "ISBN", "Book Name", "Author", "Available", "Total", "Price"));
        System.out.println("-".repeat(97));
        for (Book b : books) {
            System.out.println(String.format("%-10s %-35s %-22s %9d %6d   Rs%-6.0f",
                    b.getIsbn(), b.getName(), b.getAuthor(),
                    b.getAvailableQuantity(), b.getTotalQuantity(), b.getPrice()));
        }
    }

    private void userManagement() {
        while (true) {
            System.out.println("\n--- User Management ---");
            System.out.println("1. Add Admin");
            System.out.println("2. Add Borrower");
            System.out.println("3. View All Users");
            System.out.println("4. Manage Borrower Security Deposit");
            System.out.println("5. Back");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: addAdmin();           break;
                case 2: addBorrower();        break;
                case 3: viewAllUsers();       break;
                case 4: manageDeposit();      break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addAdmin() {
        System.out.print("Email    : "); String email    = scanner.nextLine().trim();
        if (DataStore.findUserByEmail(email) != null) {
            System.out.println("This email is already registered."); return;
        }
        System.out.print("Password : "); String password = scanner.nextLine().trim();
        System.out.print("Name     : "); String name     = scanner.nextLine().trim();
        DataStore.users.add(new Admin(email, password, name));
        System.out.println("Admin account created for: " + name);
    }

    private void addBorrower() {
        System.out.print("Email    : "); String email    = scanner.nextLine().trim();
        if (DataStore.findUserByEmail(email) != null) {
            System.out.println("This email is already registered."); return;
        }
        System.out.print("Password : "); String password = scanner.nextLine().trim();
        System.out.print("Name     : "); String name     = scanner.nextLine().trim();
        DataStore.users.add(new Borrower(email, password, name));
        System.out.println("Borrower account created for: " + name + " | Initial deposit: Rs1500");
    }

    private void viewAllUsers() {
        System.out.println("\n" + String.format("%-32s %-22s %-10s %-12s", "Email", "Name", "Role", "Deposit"));
        System.out.println("-".repeat(78));
        for (User u : DataStore.users) {
            String deposit = u.getRole().equals("BORROWER")
                    ? "Rs" + String.format("%.0f", ((Borrower) u).getSecurityDeposit())
                    : "-";
            System.out.println(String.format("%-32s %-22s %-10s %-12s",
                    u.getEmail(), u.getName(), u.getRole(), deposit));
        }
    }

    private void manageDeposit() {
        System.out.print("Enter Borrower Email: ");
        String email = scanner.nextLine().trim();
        User user = DataStore.findUserByEmail(email);
        if (user == null || !user.getRole().equals("BORROWER")) {
            System.out.println("Borrower not found."); return;
        }
        Borrower borrower = (Borrower) user;
        System.out.println("Current deposit: Rs" + String.format("%.0f", borrower.getSecurityDeposit()));
        System.out.println("1. Add to deposit   2. Deduct from deposit   3. Back");
        System.out.print("Choose: ");
        int choice = readInt();
        if (choice == 1) {
            System.out.print("Amount to add: Rs");
            double amt = readDouble();
            borrower.setSecurityDeposit(borrower.getSecurityDeposit() + amt);
            System.out.println("Updated deposit: Rs" + String.format("%.0f", borrower.getSecurityDeposit()));
        } else if (choice == 2) {
            System.out.print("Amount to deduct: Rs");
            double amt = readDouble();
            borrower.setSecurityDeposit(borrower.getSecurityDeposit() - amt);
            System.out.println("Updated deposit: Rs" + String.format("%.0f", borrower.getSecurityDeposit()));
        }
    }

    private void reports() {
        while (true) {
            System.out.println("\n--- Reports ---");
            System.out.println("1. Books with Low Stock");
            System.out.println("2. Books Never Borrowed");
            System.out.println("3. Heavily Borrowed Books");
            System.out.println("4. Students with Outstanding Books (by date)");
            System.out.println("5. Book Status by ISBN");
            System.out.println("6. Back");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: lowStockReport();       break;
                case 2: neverBorrowedReport();  break;
                case 3: heavilyBorrowedReport();break;
                case 4: outstandingReport();    break;
                case 5: bookStatusReport();     break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void lowStockReport() {
        System.out.print("Show books with available quantity <= : ");
        int threshold = readInt();
        List<Book> books = reportService.getLowStockBooks(threshold);
        if (books.isEmpty()) { System.out.println("No books below this threshold."); return; }
        System.out.println("\nLow Stock Books:");
        printBookTable(books);
    }

    private void neverBorrowedReport() {
        List<Book> books = reportService.getBooksNeverBorrowed();
        if (books.isEmpty()) { System.out.println("Every book has been borrowed at least once."); return; }
        System.out.println("\nBooks Never Borrowed:");
        printBookTable(books);
    }

    private void heavilyBorrowedReport() {
        System.out.print("Show top how many books? : ");
        int n = readInt();
        List<Book> books = reportService.getHeavilyBorrowedBooks(n);
        System.out.println("\nHeavily Borrowed Books:");
        System.out.println(String.format("%-10s %-35s %-22s %s",
                "ISBN", "Book Name", "Author", "Total Borrows"));
        System.out.println("-".repeat(85));
        for (Book b : books) {
            System.out.println(String.format("%-10s %-35s %-22s %d",
                    b.getIsbn(), b.getName(), b.getAuthor(), b.getTotalBorrowCount()));
        }
    }

    private void outstandingReport() {
        System.out.print("Enter date (DD/MM/YYYY): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, fmt);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use DD/MM/YYYY."); return;
        }
        List<BorrowRecord> records = reportService.getOutstandingBorrows(date);
        if (records.isEmpty()) { System.out.println("No outstanding borrows as on " + dateStr + "."); return; }
        System.out.println("\nOutstanding Borrows as on " + dateStr + ":");
        System.out.println(String.format("%-8s %-22s %-35s %-12s %-12s",
                "ID", "Borrower Name", "Book", "Borrow Date", "Due Date"));
        System.out.println("-".repeat(93));
        for (BorrowRecord r : records) {
            User u = DataStore.findUserByEmail(r.getBorrowerEmail());
            String bName = (u != null) ? u.getName() : r.getBorrowerEmail();
            System.out.println(String.format("%-8s %-22s %-35s %-12s %-12s",
                    r.getRecordId(), bName, r.getBookName(),
                    r.getBorrowDate(), r.getDueDate()));
        }
    }

    private void bookStatusReport() {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        Book book = DataStore.findBookByIsbn(isbn);
        if (book == null) { System.out.println("Book not found."); return; }

        System.out.println("\nBook  : " + book.getName());
        System.out.println("Stock : " + book.getAvailableQuantity() + " available / " + book.getTotalQuantity() + " total");

        List<BorrowRecord> activeRecords = reportService.getActiveRecordsByIsbn(isbn);
        if (activeRecords.isEmpty()) {
            System.out.println("Status: All copies are currently on the rack.");
        } else {
            System.out.println("Currently borrowed by:");
            System.out.println(String.format("  %-22s %-14s %-14s", "Borrower Name", "Borrowed On", "Expected Return"));
            for (BorrowRecord r : activeRecords) {
                User u = DataStore.findUserByEmail(r.getBorrowerEmail());
                String bName = (u != null) ? u.getName() : r.getBorrowerEmail();
                System.out.println(String.format("  %-22s %-14s %-14s", bName, r.getBorrowDate(), r.getDueDate()));
            }
        }
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }

    private double readDouble() {
        try { return Double.parseDouble(scanner.nextLine().trim()); }
        catch (Exception e) { return 0.0; }
    }
}
