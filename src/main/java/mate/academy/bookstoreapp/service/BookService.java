package mate.academy.bookstoreapp.service;

import java.util.List;
import mate.academy.bookstoreapp.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
