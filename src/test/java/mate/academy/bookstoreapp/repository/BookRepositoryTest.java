package mate.academy.bookstoreapp.repository;

import java.util.Set;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.model.Category;
import mate.academy.bookstoreapp.repository.book.BookRepository;
import mate.academy.bookstoreapp.utils.StarterBook;
import mate.academy.bookstoreapp.utils.StarterCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find book with category by id")
    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_BookWithCategory_ReturnsBook() {
        Category category = new Category();
        category.setId(1L);
        category.setName(StarterCategory.NAME);

        Set<Category> categories = Set.of(category);

        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setTitle(StarterBook.TITLE);
        expectedBook.setAuthor(StarterBook.AUTHOR);
        expectedBook.setIsbn(StarterBook.ISBN);
        expectedBook.setPrice(StarterBook.PRICE);
        expectedBook.setCategories(categories);

        Book actualBook = bookRepository.findById(1L).get();

        Assertions.assertEquals(expectedBook, actualBook);
    }
}
