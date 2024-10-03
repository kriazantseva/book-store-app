package mate.academy.bookstoreapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstoreapp.dto.book.BookDto;
import mate.academy.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreapp.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreapp.mapper.BookMapper;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.model.Category;
import mate.academy.bookstoreapp.repository.book.BookRepository;
import mate.academy.bookstoreapp.repository.category.CategoryRepository;
import mate.academy.bookstoreapp.service.impl.BookServiceImpl;
import mate.academy.bookstoreapp.utils.StarterBook;
import mate.academy.bookstoreapp.utils.StarterCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    private CreateBookRequestDto bookRequestDto;
    private BookDto bookDto;
    private Book book;
    private Category category;

    @BeforeEach
    public void setUpBookDto() {
        bookRequestDto = new CreateBookRequestDto(
                StarterBook.TITLE,
                StarterBook.AUTHOR,
                StarterBook.ISBN,
                StarterBook.PRICE,
                StarterBook.DESCRIPTION,
                StarterBook.COVER_IMAGE,
                StarterBook.CATEGORY_IDS
        );

        category = new Category();
        category.setId(1L);
        category.setName(StarterCategory.NAME);
        category.setDescription(StarterCategory.DESCRIPTION);

        List<Category> categories = List.of(category);

        book = new Book();
        book.setId(1L);
        book.setTitle(bookRequestDto.title());
        book.setAuthor(bookRequestDto.author());
        book.setIsbn(bookRequestDto.isbn());
        book.setPrice(bookRequestDto.price());
        book.setDescription(bookRequestDto.description());
        book.setCoverImage(bookRequestDto.coverImage());
        book.setCategories(new HashSet<>(Set.of(categories.get(0))));

        bookDto = new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getCoverImage(),
                Set.of(1L)
        );
    }

    @AfterEach
    public void tearDown() {
        bookRequestDto = null;
        bookDto = null;
        book = null;
        category = null;
    }

    @Test
    public void save_valid_CreateBookRequestDto_returns_BookDto() {
        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(bookRequestDto);

        assertEquals(bookDto, result);

        verify(bookMapper).toModel(bookRequestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @DisplayName("Get a book by ID - Valid ID")
    @Test
    public void findById_existingId_returns_BookDto() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.findById(bookId);

        assertNotNull(result);
        assertEquals(bookDto, result);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(book);
    }

    @DisplayName("Get a book by ID - Non existing ID - throws exception")
    @Test
    public void findById_nonExistingId_throwsEntityNotFoundException() {
        Long bookId = 2L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.findById(bookId);
        });

        assertEquals("Can`t find the book with id" + bookId, exception.getMessage());

        verify(bookRepository).findById(bookId);
        verify(bookMapper, never()).toDto(any());
    }

    @DisplayName("Get all books - with pagination")
    @Test
    public void findAll_returns_ListOfBookDto() {
        Book book2 = createSecondBook();

        BookDto bookDto2 = new BookDto(
                book2.getId(),
                book2.getTitle(),
                book2.getAuthor(),
                book2.getIsbn(),
                book2.getPrice(),
                book2.getDescription(),
                book2.getCoverImage(),
                Set.of(1L)
        );
        Pageable pageable = PageRequest.of(0, 5);
        List<Book> books = List.of(book, book2);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDtoList(bookPage.getContent())).thenReturn(List.of(bookDto, bookDto2));

        List<BookDto> expected = List.of(bookDto, bookDto2);
        List<BookDto> actual = bookService.findAll(pageable);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @DisplayName("Get all books - test with 100 books - with pagination")
    @Test
    public void findAll_with100Books_pageable() {
        List<Book> books = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Book bookForPageable = new Book();
            bookForPageable.setId(Long.valueOf(i));
            bookForPageable.setTitle("Book " + i);
            bookForPageable.setAuthor("Author");
            bookForPageable.setIsbn("000-1-00-111111-" + i);
            bookForPageable.setPrice(BigDecimal.valueOf(10.99));
            bookForPageable.setDescription("Description of book ");
            bookForPageable.setCoverImage("cover.jpeg");
            bookForPageable.setCategories(Set.of(category));

            books.add(bookForPageable);
        }

        List<BookDto> bookDtos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Book b = books.get(i);
            BookDto bookDtoForPageable = new BookDto(
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getIsbn(),
                    b.getPrice(),
                    b.getDescription(),
                    b.getCoverImage(),
                    Set.of(1L)
            );
            bookDtos.add(bookDtoForPageable);
        }

        Pageable pageableFirst10 = PageRequest.of(0, 10);
        Pageable pageableMiddle50 = PageRequest.of(4, 10);
        Page<Book> bookPage1 = new PageImpl<>(books, pageableFirst10, books.size());
        Page<Book> bookPage2 = new PageImpl<>(books, pageableMiddle50, books.size());

        List<BookDto> first10BookDtos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            first10BookDtos.add(bookDtos.get(i));
        }

        List<BookDto> middle50BookDtos = new ArrayList<>();
        for (int i = 40; i < 50; i++) {
            middle50BookDtos.add(bookDtos.get(i));
        }

        when(bookRepository.findAll(pageableFirst10)).thenReturn(bookPage1);
        when(bookMapper.toDtoList(bookPage1.getContent())).thenReturn(first10BookDtos);
        List<BookDto> expectedFirst10 = first10BookDtos;
        List<BookDto> actualFirst10 = bookService.findAll(pageableFirst10);

        when(bookRepository.findAll(pageableMiddle50)).thenReturn(bookPage2);
        when(bookMapper.toDtoList(bookPage2.getContent())).thenReturn(middle50BookDtos);
        List<BookDto> expectedMiddle50 = middle50BookDtos;
        List<BookDto> actualMiddle50 = bookService.findAll(pageableMiddle50);

        assertEquals(expectedFirst10, actualFirst10);
        assertEquals(10, actualMiddle50.size());
        assertEquals(expectedMiddle50, actualMiddle50);
        assertEquals(Optional.of(41L), Optional.ofNullable(actualMiddle50.get(0).id()));
    }

    @DisplayName("Update a book - Returns updated book")
    @Test
    public void valid_update_with_CreateBookRequestDto() {
        CreateBookRequestDto updateBookDto = new CreateBookRequestDto(
                "New Book",
                "Some Author",
                "000-1-00-111111-0",
                BigDecimal.valueOf(10.99),
                "Adventures of Someone Else",
                "cover.jpeg",
                List.of()
        );

        BookDto updatedBookDto = new BookDto(
                1L,
                "New Book",
                "Some Author",
                "000-1-00-111111-0",
                BigDecimal.valueOf(10.99),
                "Adventures of Someone Else",
                "cover.jpeg",
                Set.of()
        );
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateBookFromDto(updateBookDto, book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(updatedBookDto);

        BookDto result = bookService.update(bookId, updateBookDto);

        assertNotNull(result);
        assertEquals(updatedBookDto, result);
        assertEquals("New Book", result.title());

        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateBookFromDto(updateBookDto, book);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @DisplayName("Delete book")
    @Test
    public void delete_book_withValid_bookId() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(bookId);
        bookService.deleteById(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), bookRepository.findById(bookId));

        verify(bookRepository).deleteById(bookId);
    }

    @DisplayName("Find all books - With Category ID")
    @Test
    public void findAllBooks_withValid_categoryId_returns_ListBookDtoWithoutCategoryIds() {
        Book book2 = createSecondBook();

        BookDtoWithoutCategoryIds bookDtoWithout = new BookDtoWithoutCategoryIds(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getCoverImage()
        );

        BookDtoWithoutCategoryIds bookDtoWithout2 = new BookDtoWithoutCategoryIds(
                book2.getId(),
                book2.getTitle(),
                book2.getAuthor(),
                book2.getIsbn(),
                book2.getPrice(),
                book2.getDescription(),
                book2.getCoverImage()
        );

        Long categoryId = 1L;
        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(book, book2));
        when(bookMapper.toDtoWithoutCategoriesList(List.of(book, book2)))
                .thenReturn(List.of(bookDtoWithout, bookDtoWithout2));

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithout, bookDtoWithout2);
        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(categoryId);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @DisplayName("Add category to book")
    @Test
    public void addValidCategory_toBookWithValidId() {
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Novel");
        category2.setDescription("Novel description");

        Book book2 = createSecondBook();
        book2.setCategories(new HashSet<>());

        Long bookId = 2L;
        String categoryName = "novel";

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book2));
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category2));
        when(bookRepository.findAll()).thenReturn(List.of(book, book2));

        bookService.addCategoryToBookById(bookId, categoryName);
        List<Book> actual = bookRepository.findAll();

        assertNotNull(actual);
        assertEquals(2, actual.size());

        Book updatedBook = actual.get(1);
        assertTrue(updatedBook.getCategories().contains(category2));
    }

    @DisplayName("Remove category from book")
    @Test
    public void removeValidCategory_fromBookWithValidId() {
        Long bookId = 1L;
        String categoryName = "detective";

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));
        when(bookRepository.findAll()).thenReturn(List.of(book));

        bookService.removeCategoryFromBookById(bookId, categoryName);
        List<Book> actual = bookRepository.findAll();

        assertNotNull(actual);

        Book updatedBook = actual.get(0);
        assertFalse(updatedBook.getCategories().contains(category));
    }

    private Book createSecondBook() {
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book");
        book2.setAuthor("Author");
        book2.setIsbn("000-1-00-111111-1");
        book2.setPrice(BigDecimal.valueOf(10.99));
        book2.setDescription("Book description");
        book2.setCoverImage("cover.jpeg");
        book2.setCategories(Set.of(category));
        return book2;
    }
}
