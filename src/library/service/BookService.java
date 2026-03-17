package library.service;

import library.data.DataStore;
import library.model.Book;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BookService {

    public boolean addBook(String isbn, String name, String author, int quantity, double price) {
        if (DataStore.findBookByIsbn(isbn) != null) {
            return false;
        }
        DataStore.books.add(new Book(isbn, name, author, quantity, price));
        return true;
    }

    public boolean deleteBook(String isbn) {
        Book book = DataStore.findBookByIsbn(isbn);
        if (book == null) return false;
        DataStore.books.remove(book);
        return true;
    }

    public List<Book> getAllBooksSortedByName() {
        List<Book> sorted = new ArrayList<>(DataStore.books);
        sorted.sort(Comparator.comparing(Book::getName));
        return sorted;
    }

    public List<Book> getAllBooksSortedByAvailableQty() {
        List<Book> sorted = new ArrayList<>(DataStore.books);
        sorted.sort(Comparator.comparingInt(Book::getAvailableQuantity));
        return sorted;
    }

    public List<Book> searchByName(String name) {
        List<Book> result = new ArrayList<>();
        for (Book book : DataStore.books) {
            if (book.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public Book searchByIsbn(String isbn) {
        return DataStore.findBookByIsbn(isbn);
    }
}
