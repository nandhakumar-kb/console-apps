package library.service;

import library.data.DataStore;
import library.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowService {

    public static final double MIN_DEPOSIT = 500.0;
    public static final int    MAX_BOOKS   = 3;

    public List<BorrowRecord> getActiveBorrows(String borrowerEmail) {
        List<BorrowRecord> active = new ArrayList<>();
        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getBorrowerEmail().equalsIgnoreCase(borrowerEmail)
                    && !r.isReturned() && !r.isLost()) {
                active.add(r);
            }
        }
        return active;
    }

    public List<BorrowRecord> getBorrowHistory(String borrowerEmail) {
        List<BorrowRecord> history = new ArrayList<>();
        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getBorrowerEmail().equalsIgnoreCase(borrowerEmail)
                    && (r.isReturned() || r.isLost())) {
                history.add(r);
            }
        }
        return history;
    }

    public BorrowRecord findActiveBorrow(String borrowerEmail, String isbn) {
        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getBorrowerEmail().equalsIgnoreCase(borrowerEmail)
                    && r.getIsbn().equalsIgnoreCase(isbn)
                    && !r.isReturned() && !r.isLost()) {
                return r;
            }
        }
        return null;
    }

    public boolean addToCart(Borrower borrower, String isbn) {
        List<BorrowRecord> active = getActiveBorrows(borrower.getEmail());

        for (BorrowRecord r : active) {
            if (r.getIsbn().equalsIgnoreCase(isbn)) return false;
        }
        for (String cartIsbn : borrower.getCart()) {
            if (cartIsbn.equalsIgnoreCase(isbn)) return false;
        }
        if (active.size() + borrower.getCart().size() >= MAX_BOOKS) return false;

        borrower.getCart().add(isbn);
        return true;
    }

    public boolean removeFromCart(Borrower borrower, String isbn) {
        return borrower.getCart().removeIf(s -> s.equalsIgnoreCase(isbn));
    }

    public String checkout(Borrower borrower) {
        if (borrower.getSecurityDeposit() < MIN_DEPOSIT) {
            return "Cannot checkout. Minimum security deposit of Rs500 required.";
        }
        List<String> cart = borrower.getCart();
        if (cart.isEmpty()) {
            return "Your cart is empty.";
        }
        List<BorrowRecord> active = getActiveBorrows(borrower.getEmail());
        if (active.size() + cart.size() > MAX_BOOKS) {
            return "Cannot borrow more than " + MAX_BOOKS + " books at a time.";
        }
        for (String isbn : cart) {
            Book book = DataStore.findBookByIsbn(isbn);
            if (book == null || book.getAvailableQuantity() <= 0) {
                return "Book " + isbn + " is no longer available. Please update your cart.";
            }
        }
        for (String isbn : cart) {
            Book book = DataStore.findBookByIsbn(isbn);
            book.setAvailableQuantity(book.getAvailableQuantity() - 1);
            book.incrementBorrowCount();
            DataStore.borrowRecords.add(new BorrowRecord(
                    DataStore.generateRecordId(),
                    borrower.getEmail(),
                    isbn,
                    book.getName(),
                    LocalDate.now()));
        }
        cart.clear();
        return "Checkout successful! Books are due in 15 days.";
    }

    public String extendTenure(BorrowRecord record) {
        if (record.getExtensionCount() >= 2) {
            return "Extension limit reached. A book can only be extended 2 times.";
        }
        record.extendDueDate();
        return "Tenure extended. New due date: " + record.getDueDate();
    }

    public void returnBook(BorrowRecord record, LocalDate returnDate) {
        record.setReturned(true);
        record.setReturnDate(returnDate);
        Book book = DataStore.findBookByIsbn(record.getIsbn());
        if (book != null) {
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        }
    }

    public void markBookAsLost(BorrowRecord record) {
        record.setLost(true);
        record.setReturnDate(LocalDate.now());
    }

    public String exchangeBook(Borrower borrower, String oldIsbn, String newIsbn) {
        BorrowRecord oldRecord = findActiveBorrow(borrower.getEmail(), oldIsbn);
        if (oldRecord == null) return "No active borrow found for ISBN: " + oldIsbn;

        Book newBook = DataStore.findBookByIsbn(newIsbn);
        if (newBook == null)                     return "Replacement book not found.";
        if (newBook.getAvailableQuantity() <= 0) return "Replacement book is not available.";

        for (BorrowRecord r : getActiveBorrows(borrower.getEmail())) {
            if (r.getIsbn().equalsIgnoreCase(newIsbn)) {
                return "You already have that book borrowed.";
            }
        }
        Book oldBook = DataStore.findBookByIsbn(oldIsbn);
        oldRecord.setReturned(true);
        oldRecord.setReturnDate(LocalDate.now());
        if (oldBook != null) {
            oldBook.setAvailableQuantity(oldBook.getAvailableQuantity() + 1);
        }
        newBook.setAvailableQuantity(newBook.getAvailableQuantity() - 1);
        newBook.incrementBorrowCount();
        BorrowRecord newRecord = new BorrowRecord(
                DataStore.generateRecordId(),
                borrower.getEmail(),
                newIsbn,
                newBook.getName(),
                LocalDate.now());
        DataStore.borrowRecords.add(newRecord);
        return "Exchange successful! New book due date: " + newRecord.getDueDate();
    }
}
