package mate.academy.bookstoreapp.service;

import java.util.List;
import mate.academy.bookstoreapp.dto.book.BookDto;
import mate.academy.bookstoreapp.dto.book.BookSearchParametersDto;
import mate.academy.bookstoreapp.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> findAll(Pageable pageable);

    List<BookDto> search(BookSearchParametersDto searchParametersDto);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
