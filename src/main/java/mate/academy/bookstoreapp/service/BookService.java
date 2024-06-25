package mate.academy.bookstoreapp.service;

import java.util.List;
import mate.academy.bookstoreapp.dto.book.BookDto;
import mate.academy.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreapp.dto.book.BookSearchParametersDto;
import mate.academy.bookstoreapp.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto, String categoryName);

    BookDto findById(Long id);

    List<BookDto> findAll(Pageable pageable);

    List<BookDto> search(BookSearchParametersDto searchParametersDto);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);

    void addCategoryToBookById(Long id, String categoryName);

    void removeCategoryFromBookById(Long id, String categoryName);
}
