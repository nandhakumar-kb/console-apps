package vehiclerental.model;

import java.time.LocalDate;

public class FineRecord {
    private final String fineId;
    private final String borrowerEmail;
    private final String reason;
    private final double amount;
    private final String paymentMode;
    private final LocalDate date;

    public FineRecord(String fineId, String borrowerEmail, String reason,
            double amount, String paymentMode, LocalDate date) {
        this.fineId = fineId;
        this.borrowerEmail = borrowerEmail;
        this.reason = reason;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.date = date;
    }

    public String getFineId() {
        return fineId;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public String getReason() {
        return reason;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Rs%.0f | %s | %s", fineId, reason, amount, paymentMode, date);
    }
}
