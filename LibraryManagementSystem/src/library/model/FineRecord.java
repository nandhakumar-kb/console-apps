package library.model;

import java.time.LocalDate;

public class FineRecord {
    private String    borrowerEmail;
    private String    reason;
    private double    amount;
    private LocalDate date;
    private String    paymentMode;

    public FineRecord(String borrowerEmail, String reason, double amount, LocalDate date) {
        this.borrowerEmail = borrowerEmail;
        this.reason        = reason;
        this.amount        = amount;
        this.date          = date;
        this.paymentMode   = "Pending";
    }

    public String    getBorrowerEmail() { return borrowerEmail; }
    public String    getReason()        { return reason; }
    public double    getAmount()        { return amount; }
    public LocalDate getDate()          { return date; }
    public String    getPaymentMode()   { return paymentMode; }

    public void setPaymentMode(String mode) { this.paymentMode = mode; }

    @Override
    public String toString() {
        return String.format("Date: %-12s | Reason: %-40s | Amount: Rs%-6.0f | Paid via: %s",
                date, reason, amount, paymentMode);
    }
}
