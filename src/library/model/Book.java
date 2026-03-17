package library.model;

public class Book {
    private String isbn;
    private String name;
    private String author;
    private int    totalQuantity;
    private int    availableQuantity;
    private double price;
    private int    totalBorrowCount;

    public Book(String isbn, String name, String author, int totalQuantity, double price) {
        this.isbn              = isbn;
        this.name              = name;
        this.author            = author;
        this.totalQuantity     = totalQuantity;
        this.availableQuantity = totalQuantity;
        this.price             = price;
        this.totalBorrowCount  = 0;
    }

    public String getIsbn()             { return isbn; }
    public String getName()             { return name; }
    public String getAuthor()           { return author; }
    public int    getTotalQuantity()    { return totalQuantity; }
    public int    getAvailableQuantity(){ return availableQuantity; }
    public double getPrice()            { return price; }
    public int    getTotalBorrowCount() { return totalBorrowCount; }

    public void setName(String name)              { this.name = name; }
    public void setAuthor(String author)          { this.author = author; }
    public void setTotalQuantity(int qty)         { this.totalQuantity = qty; }
    public void setAvailableQuantity(int qty)     { this.availableQuantity = qty; }
    public void setPrice(double price)            { this.price = price; }
    public void incrementBorrowCount()            { this.totalBorrowCount++; }

    @Override
    public String toString() {
        return String.format("%-10s %-35s %-20s Avail: %d/%d  Price: Rs%.0f",
                isbn, name, author, availableQuantity, totalQuantity, price);
    }
}
