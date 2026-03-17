package library.service;

import library.data.DataStore;
import library.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportService {

    public List<Book> getLowStockBooks(int threshold) {
        List<Book> result = new ArrayList<>();
        for (Book book : DataStore.books) {
            if (book.getAvailableQuantity() <= threshold) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> getBooksNeverBorrowed() {
        List<Book> result = new ArrayList<>();
        for (Book book : DataStore.books) {
            if (book.getTotalBorrowCount() == 0) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> getHeavilyBorrowedBooks(int topN) {
        List<Book> sorted = new ArrayList<>(DataStore.books);
        sorted.sort((a, b) -> b.getTotalBorrowCount() - a.getTotalBorrowCount());
        return sorted.subList(0, Math.min(topN, sorted.size()));
    }

    public List<BorrowRecord> getOutstandingBorrows(LocalDate asOfDate) {
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord r : DataStore.borrowRecords) {
            if (!r.isReturned() && !r.isLost() && !r.getBorrowDate().isAfter(asOfDate)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<BorrowRecord> getActiveRecordsByIsbn(String isbn) {
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getIsbn().equalsIgnoreCase(isbn) && !r.isReturned() && !r.isLost()) {
                result.add(r);
            }
        }
        return result;
    }

    public List<FineRecord> getFinesForBorrower(String borrowerEmail) {
        List<FineRecord> result = new ArrayList<>();
        for (FineRecord f : DataStore.fineRecords) {
            if (f.getBorrowerEmail().equalsIgnoreCase(borrowerEmail)) {
                result.add(f);
            }
        }
        return result;
    }
}
