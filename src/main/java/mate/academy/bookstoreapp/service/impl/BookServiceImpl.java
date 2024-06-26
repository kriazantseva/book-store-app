package mate.academy.bookstoreapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.book.BookDto;
import mate.academy.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreapp.dto.book.BookSearchParametersDto;
import mate.academy.bookstoreapp.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreapp.mapper.BookMapper;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.model.Category;
import mate.academy.bookstoreapp.repository.book.BookRepository;
import mate.academy.bookstoreapp.repository.book.BookSpecificationBuilder;
import mate.academy.bookstoreapp.repository.category.CategoryRepository;
import mate.academy.bookstoreapp.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find the book with id" + id));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return bookMapper.toDtoList(bookPage.getContent());
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParametersDto) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParametersDto);
        return bookMapper.toDtoList(bookRepository.findAll(bookSpecification));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = findBookById(id);
        bookMapper.updateBookFromDto(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId) {
        List<Book> books = bookRepository.findAllByCategoryId(categoryId);
        return bookMapper.toDtoWithoutCategoriesList(books);
    }

    @Override
    public void addCategoryToBookById(Long id, String categoryName) {
        Book book = findBookById(id);
        Category approvedCategory = findCategoryByName(categoryName);
        book.getCategories().add(approvedCategory);
        bookRepository.save(book);
    }

    @Override
    public void removeCategoryFromBookById(Long id, String categoryName) {
        Book book = findBookById(id);
        Category approvedCategory = findCategoryByName(categoryName);
        book.getCategories().remove(approvedCategory);
        bookRepository.save(book);
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find the book with id" + id));
    }

    private Category findCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName.toLowerCase())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find the category with name" + categoryName
                ));
    }
}
