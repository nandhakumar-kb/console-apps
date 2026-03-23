package library.service;

import library.data.DataStore;
import library.model.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineService {

    public double calculateLateFine(BorrowRecord record, LocalDate returnDate) {
        if (!returnDate.isAfter(record.getDueDate())) {
            return 0.0;
        }
        long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), returnDate);
        Book   book      = DataStore.findBookByIsbn(record.getIsbn());
        double bookPrice = (book != null) ? book.getPrice() : 0.0;

        double fine       = 0.0;
        long   remaining  = overdueDays;
        double ratePerDay = 2.0;

        while (remaining > 0) {
            long daysInBlock = Math.min(remaining, 10);
            fine += daysInBlock * ratePerDay;
            remaining  -= daysInBlock;
            ratePerDay *= 2;
        }

        double maxFine = 0.80 * bookPrice;
        return Math.min(fine, maxFine);
    }

    public double calculateLostBookFine(BorrowRecord record) {
        Book book = DataStore.findBookByIsbn(record.getIsbn());
        return (book != null) ? 0.50 * book.getPrice() : 0.0;
    }

    public double getMemberCardLostFine() {
        return 10.0;
    }

    public void applyFine(Borrower borrower, String reason, double amount, boolean payByCash) {
        FineRecord fineRecord = new FineRecord(borrower.getEmail(), reason, amount, LocalDate.now());
        if (payByCash) {
            fineRecord.setPaymentMode("Cash");
        } else {
            fineRecord.setPaymentMode("Security Deposit");
            borrower.setSecurityDeposit(borrower.getSecurityDeposit() - amount);
        }
        DataStore.fineRecords.add(fineRecord);
    }
}
