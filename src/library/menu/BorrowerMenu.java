package library.menu;

import library.data.DataStore;
import library.model.*;
import library.service.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class BorrowerMenu {

    private final Scanner   scanner;
    private final Borrower      borrower;
    private final BorrowService borrowService  = new BorrowService();
    private final FineService   fineService    = new FineService();
    private final ReportService reportService  = new ReportService();
    private final BookService   bookService    = new BookService();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BorrowerMenu(Scanner scanner, Borrower borrower) {
        this.scanner  = scanner;
        this.borrower = borrower;
    }

    public void show() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  BORROWER MENU  |  " + borrower.getName());
            System.out.println("  Security Deposit: Rs" +
                    String.format("%.0f", borrower.getSecurityDeposit()));
            System.out.println("========================================");
            System.out.println("1. View Available Books");
            System.out.println("2. Manage Cart");
            System.out.println("3. My Current Borrowings");
            System.out.println("4. Return / Transactions");
            System.out.println("5. Report Membership Card Lost");
            System.out.println("6. My History");
            System.out.println("7. Logout");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: viewAvailableBooks();  break;
                case 2: manageCart();          break;
                case 3: viewCurrentBorrowings(); break;
                case 4: transactions();        break;
                case 5: reportCardLost();      break;
                case 6: history();             break;
                case 7:
                    System.out.println("Logged out. Goodbye, " + borrower.getName() + "!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAvailableBooks() {
        List<Book> books = bookService.getAllBooksSortedByName();
        System.out.println("\n--- Available Books ---");
        System.out.println(String.format("%-10s %-35s %-22s %9s %10s",
                "ISBN", "Book Name", "Author", "Available", "Price"));
        System.out.println("-".repeat(92));
        boolean anyAvailable = false;
        for (Book b : books) {
            if (b.getAvailableQuantity() > 0) {
                System.out.println(String.format("%-10s %-35s %-22s %9d   Rs%-6.0f",
                        b.getIsbn(), b.getName(), b.getAuthor(),
                        b.getAvailableQuantity(), b.getPrice()));
                anyAvailable = true;
            }
        }
        if (!anyAvailable) System.out.println("No books currently available.");
    }

    private void manageCart() {
        while (true) {
            int activeCount = borrowService.getActiveBorrows(borrower.getEmail()).size();
            System.out.println("\n--- Cart ---");
            System.out.println("Cart: " + borrower.getCart().size() + " item(s) | Active borrows: "
                    + activeCount + " | Combined max: 3");
            System.out.println("1. Add Book to Cart");
            System.out.println("2. Remove Book from Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Back");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: addToCart();   break;
                case 2: removeFromCart(); break;
                case 3: viewCart();    break;
                case 4:
                    System.out.println(borrowService.checkout(borrower));
                    break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addToCart() {
        System.out.println("Find book by: 1. ISBN   2. Name");
        System.out.print("Choose: ");
        int choice = readInt();
        Book book = null;

        if (choice == 1) {
            System.out.print("Enter ISBN: ");
            book = DataStore.findBookByIsbn(scanner.nextLine().trim());
        } else {
            System.out.print("Enter name (partial match ok): ");
            List<Book> results = bookService.searchByName(scanner.nextLine().trim());
            if (results.isEmpty()) { System.out.println("No books found."); return; }
            if (results.size() == 1) {
                book = results.get(0);
            } else {
                for (int i = 0; i < results.size(); i++) {
                    System.out.println((i + 1) + ". " + results.get(i));
                }
                System.out.print("Select number: ");
                int sel = readInt();
                if (sel >= 1 && sel <= results.size()) book = results.get(sel - 1);
            }
        }

        if (book == null)                           { System.out.println("Book not found."); return; }
        if (book.getAvailableQuantity() <= 0)       { System.out.println("Book not available."); return; }

        if (borrowService.addToCart(borrower, book.getIsbn())) {
            System.out.println("'" + book.getName() + "' added to cart.");
        } else {
            System.out.println("Cannot add: book already in cart/borrowed, or you have reached the 3-book limit.");
        }
    }

    private void removeFromCart() {
        if (borrower.getCart().isEmpty()) { System.out.println("Cart is empty."); return; }
        viewCart();
        System.out.print("Enter ISBN to remove: ");
        if (borrowService.removeFromCart(borrower, scanner.nextLine().trim())) {
            System.out.println("Book removed from cart.");
        } else {
            System.out.println("That ISBN is not in your cart.");
        }
    }

    private void viewCart() {
        if (borrower.getCart().isEmpty()) { System.out.println("Your cart is empty."); return; }
        System.out.println("\nYour Cart:");
        for (String isbn : borrower.getCart()) {
            Book b = DataStore.findBookByIsbn(isbn);
            System.out.println("  " + isbn + " - " + (b != null ? b.getName() : "Unknown"));
        }
    }

    private void viewCurrentBorrowings() {
        List<BorrowRecord> active = borrowService.getActiveBorrows(borrower.getEmail());
        if (active.isEmpty()) { System.out.println("You have no active borrowings."); return; }
        LocalDate today = LocalDate.now();
        System.out.println("\n--- Current Borrowings ---");
        System.out.println(String.format("%-8s %-35s %-12s %-12s %-12s",
                "ID", "Book Name", "Borrow Date", "Due Date", "Extensions"));
        System.out.println("-".repeat(83));
        for (BorrowRecord r : active) {
            String overdue = r.getDueDate().isBefore(today) ? " [OVERDUE]" : "";
            System.out.println(String.format("%-8s %-35s %-12s %-12s %-12d%s",
                    r.getRecordId(), r.getBookName(),
                    r.getBorrowDate(), r.getDueDate(),
                    r.getExtensionCount(), overdue));
        }
    }

    private void transactions() {
        List<BorrowRecord> active = borrowService.getActiveBorrows(borrower.getEmail());
        if (active.isEmpty()) { System.out.println("You have no active borrowings."); return; }

        viewCurrentBorrowings();
        System.out.print("\nEnter ISBN of the book for transaction: ");
        String isbn = scanner.nextLine().trim();
        BorrowRecord record = borrowService.findActiveBorrow(borrower.getEmail(), isbn);
        if (record == null) { System.out.println("No active borrow found for that ISBN."); return; }

        System.out.println("\nBook: " + record.getBookName());
        System.out.println("1. Extend Tenure (+15 days)");
        System.out.println("2. Return Book");
        System.out.println("3. Exchange for Another Book");
        System.out.println("4. Mark Book as Lost");
        System.out.println("5. Back");
        System.out.print("Choose: ");
        int choice = readInt();
        switch (choice) {
            case 1: extendTenure(record);  break;
            case 2: returnBook(record);    break;
            case 3: exchangeBook(record);  break;
            case 4: markBookLost(record);  break;
            case 5: return;
            default: System.out.println("Invalid choice.");
        }
    }

    private void extendTenure(BorrowRecord record) {
        System.out.println(borrowService.extendTenure(record));
    }

    private void returnBook(BorrowRecord record) {
        System.out.print("Enter return date (DD/MM/YYYY) [leave blank for today]: ");
        String dateStr  = scanner.nextLine().trim();
        LocalDate returnDate;
        if (dateStr.isEmpty()) {
            returnDate = LocalDate.now();
        } else {
            try {
                returnDate = LocalDate.parse(dateStr, fmt);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using today's date.");
                returnDate = LocalDate.now();
            }
        }
        double fine = fineService.calculateLateFine(record, returnDate);
        if (fine > 0) {
            System.out.println("Late fine: Rs" + String.format("%.0f", fine));
            fine = collectPayment(fine, "Late return - " + record.getBookName());
        } else {
            System.out.println("No late fine. Returned on time!");
        }
        borrowService.returnBook(record, returnDate);
        System.out.println("Book returned successfully.");
    }

    private void exchangeBook(BorrowRecord record) {
        LocalDate today = LocalDate.now();
        double fine = fineService.calculateLateFine(record, today);
        if (fine > 0) {
            System.out.println("Outstanding late fine for this book: Rs" + String.format("%.0f", fine));
            collectPayment(fine, "Late return (exchange) - " + record.getBookName());
        }
        System.out.print("Enter ISBN of the replacement book: ");
        String newIsbn = scanner.nextLine().trim();
        System.out.println(borrowService.exchangeBook(borrower, record.getIsbn(), newIsbn));
    }

    private void markBookLost(BorrowRecord record) {
        double fine = fineService.calculateLostBookFine(record);
        System.out.println("Lost book fine (50% of book price): Rs" + String.format("%.0f", fine));
        collectPayment(fine, "Book lost - " + record.getBookName());
        borrowService.markBookAsLost(record);
        System.out.println("Book marked as lost.");
    }

    private void reportCardLost() {
        if (borrower.isMembershipCardLost()) {
            System.out.println("Your card is already marked as lost."); return;
        }
        double fine = fineService.getMemberCardLostFine();
        System.out.println("Membership card lost fine: Rs" + String.format("%.0f", fine));
        collectPayment(fine, "Membership card lost");
        borrower.setMembershipCardLost(true);
        System.out.println("Card loss reported.");
    }

    private double collectPayment(double amount, String reason) {
        System.out.println("Pay Rs" + String.format("%.0f", amount) + " via: 1. Cash   2. Deduct from Security Deposit");
        System.out.print("Choose: ");
        int payChoice = readInt();
        boolean byCash = (payChoice == 1);
        fineService.applyFine(borrower, reason, amount, byCash);
        if (!byCash) {
            System.out.println("Remaining deposit: Rs" + String.format("%.0f", borrower.getSecurityDeposit()));
        }
        return amount;
    }

    private void history() {
        System.out.println("\n--- My History ---");
        System.out.println("1. Fine History");
        System.out.println("2. Borrow History");
        System.out.println("3. Back");
        System.out.print("Choose: ");
        int choice = readInt();
        if (choice == 1) {
            List<FineRecord> fines = reportService.getFinesForBorrower(borrower.getEmail());
            if (fines.isEmpty()) { System.out.println("No fines on your record."); return; }
            System.out.println("\n--- Fine History ---");
            for (FineRecord f : fines) System.out.println(f);
        } else if (choice == 2) {
            List<BorrowRecord> hist = borrowService.getBorrowHistory(borrower.getEmail());
            if (hist.isEmpty()) { System.out.println("No borrow history yet."); return; }
            System.out.println("\n--- Borrow History ---");
            System.out.println(String.format("%-8s %-35s %-12s %-12s %-10s",
                    "ID", "Book Name", "Borrow Date", "Return Date", "Status"));
            System.out.println("-".repeat(80));
            for (BorrowRecord r : hist) {
                String status = r.isLost() ? "Lost" : "Returned";
                System.out.println(String.format("%-8s %-35s %-12s %-12s %-10s",
                        r.getRecordId(), r.getBookName(),
                        r.getBorrowDate(), r.getReturnDate(), status));
            }
        }
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }
}
