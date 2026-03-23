package library.model;

import java.time.LocalDate;

public class BorrowRecord {
    private String    recordId;
    private String    borrowerEmail;
    private String    isbn;
    private String    bookName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean   isReturned;
    private boolean   isLost;
    private int       extensionCount;

    public BorrowRecord(String recordId, String borrowerEmail, String isbn,
                        String bookName, LocalDate borrowDate) {
        this.recordId      = recordId;
        this.borrowerEmail = borrowerEmail;
        this.isbn          = isbn;
        this.bookName      = bookName;
        this.borrowDate    = borrowDate;
        this.dueDate       = borrowDate.plusDays(15);
        this.isReturned    = false;
        this.isLost        = false;
        this.extensionCount = 0;
    }

    public String    getRecordId()      { return recordId; }
    public String    getBorrowerEmail() { return borrowerEmail; }
    public String    getIsbn()          { return isbn; }
    public String    getBookName()      { return bookName; }
    public LocalDate getBorrowDate()    { return borrowDate; }
    public LocalDate getDueDate()       { return dueDate; }
    public LocalDate getReturnDate()    { return returnDate; }
    public boolean   isReturned()       { return isReturned; }
    public boolean   isLost()           { return isLost; }
    public int       getExtensionCount(){ return extensionCount; }

    public void setReturned(boolean v)       { this.isReturned = v; }
    public void setLost(boolean v)           { this.isLost = v; }
    public void setReturnDate(LocalDate d)   { this.returnDate = d; }

    public void extendDueDate() {
        this.dueDate = this.dueDate.plusDays(15);
        this.extensionCount++;
    }
}
