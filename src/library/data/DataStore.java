package library.data;

import java.util.ArrayList;
import java.util.List;
import library.model.*;

public class DataStore {
    public static List<User>         users         = new ArrayList<>();
    public static List<Book>         books         = new ArrayList<>();
    public static List<BorrowRecord> borrowRecords = new ArrayList<>();
    public static List<FineRecord>   fineRecords   = new ArrayList<>();

    private static int recordCounter = 1000;

    static {
        users.add(new Admin   ("admin@library.com",      "admin123",    "Library Admin"));
        users.add(new Admin   ("director@library.com",   "director123", "Library Director"));
        users.add(new Borrower("pradeep@student.com",      "pradeep123",    "Pradeep"));
        users.add(new Borrower("sathya@student.com",        "sathya123",      "Sathya"));
        users.add(new Borrower("ragavan@student.com",      "ragavan123",    "Ragavan"));

        books.add(new Book("ISBN001", "Java Programming Basics",        "James Gosling",          5, 450.0));
        books.add(new Book("ISBN002", "Data Structures and Algorithms",  "Mark Allen Weiss",       3, 380.0));
        books.add(new Book("ISBN003", "Operating Systems Concepts",      "Abraham Silberschatz",   4, 520.0));
        books.add(new Book("ISBN004", "Database Management Systems",     "Ramakrishnan",           2, 490.0));
        books.add(new Book("ISBN005", "Computer Networks",               "Andrew Tanenbaum",       6, 410.0));
        books.add(new Book("ISBN006", "Discrete Mathematics",            "Kenneth Rosen",          5, 360.0));
        books.add(new Book("ISBN007", "Software Engineering",            "Roger Pressman",         3, 430.0));
        books.add(new Book("ISBN008", "Artificial Intelligence",         "Stuart Russell",         2, 580.0));
    }

    public static User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public static Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                return book;
            }
        }
        return null;
    }

    public static String generateRecordId() {
        return "BR" + (recordCounter++);
    }
}
