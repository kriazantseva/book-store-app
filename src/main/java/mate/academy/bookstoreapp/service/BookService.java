package mate.academy.bookstoreapp.service;

import java.util.List;
import mate.academy.bookstoreapp.dto.BookDto;
import mate.academy.bookstoreapp.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> findAll();
}
