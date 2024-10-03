package mate.academy.bookstoreapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.bookstoreapp.dto.book.BookDto;
import mate.academy.bookstoreapp.dto.book.BookSearchParametersDto;
import mate.academy.bookstoreapp.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreapp.utils.StarterBook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all books with pagination")
    public void findAll_ReturnsPageOfBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        List<BookDto> books = objectMapper.readValue(content,
                new TypeReference<List<BookDto>>() {});

        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        assertEquals(StarterBook.TITLE, books.get(0).title());
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get a book by ID - Valid ID")
    public void getBookById_ValidId_ReturnsBookDto() throws Exception {
        Long validId = 1L;

        MvcResult result = mockMvc.perform(get("/books/{id}", validId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        BookDto book = objectMapper.readValue(content, BookDto.class);

        assertNotNull(book);
        assertEquals(validId, book.id());
        assertEquals(StarterBook.TITLE, book.title());
        assertEquals(StarterBook.AUTHOR, book.author());
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Search books - Matching Results")
    public void searchBooks_MatchingResults_ReturnsBooks() throws Exception {
        BookSearchParametersDto searchParams = new BookSearchParametersDto(
                new String[]{"Sherlock Holmes"},
                null,
                null
        );

        String searchParamsJson = objectMapper.writeValueAsString(searchParams);

        MvcResult result = mockMvc.perform(get("/books/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(searchParamsJson))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        List<BookDto> books = objectMapper.readValue(content, new TypeReference<>() {});

        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertTrue(books.stream().anyMatch(book -> book.title().contains("Sherlock")));
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-two-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Create book - Admin User")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createBook_AdminUser_ReturnsCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = createNewBookRequestDto();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        BookDto createdBook = objectMapper.readValue(content, BookDto.class);

        assertNotNull(createdBook);
        assertEquals("Another Adventures", createdBook.title());
        assertEquals("Another Author", createdBook.author());
        assertEquals("978-3-16-148410-0", createdBook.isbn());
        assertEquals(BigDecimal.valueOf(19.99), createdBook.price());
    }

    @Test
    @DisplayName("Create book - Non-Admin User")
    @WithMockUser(username = "user", roles = {"USER"})
    public void createBook_NonAdminUser_ReturnsForbidden() throws Exception {
        CreateBookRequestDto requestDto = createNewBookRequestDto();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Update book - Admin User")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateBook_AdminUser_ReturnsUpdatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Not Sherlock Holmes",
                "Another Author",
                "978-3-16-148410-5",
                BigDecimal.valueOf(19.99),
                "Life in home",
                "cover.jpeg",
                List.of()
        );

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        BookDto updatedBook = objectMapper.readValue(content, BookDto.class);

        assertNotNull(updatedBook);
        assertEquals("Not Sherlock Holmes", updatedBook.title());
        assertEquals("Another Author", updatedBook.author());
        assertEquals("Life in home", updatedBook.description());
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Delete book - Admin User")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteBook_AdminUser_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Sql(scripts = {"classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            "classpath:databasescripts/books/add-2nd-category-to-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            "classpath:databasescripts/books/delete-2nd-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Add category to book - Admin User")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addCategoryToBook_AdminUser_ReturnsOk() throws Exception {
        String categoryName = "novel";

        mockMvc.perform(put("/books/{id}/add-category", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryName))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Remove category from book - Admin User")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void removeCategoryFromBook_AdminUser_ReturnsNoContent() throws Exception {
        String categoryName = "detective";

        mockMvc.perform(put("/books/{id}/remove-category", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryName))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private CreateBookRequestDto createNewBookRequestDto() {
        return new CreateBookRequestDto(
                "Another Adventures",
                "Another Author",
                "978-3-16-148410-0",
                BigDecimal.valueOf(19.99),
                "It's absolutely another story",
                "cover.jpeg",
                List.of(1L)
        );
    }
}
