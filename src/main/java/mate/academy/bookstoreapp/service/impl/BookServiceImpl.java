package mate.academy.bookstoreapp.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.repository.BookRepository;
import mate.academy.bookstoreapp.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }
}
