package mate.academy.bookstoreapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.logging.Logger;
import mate.academy.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreapp.dto.category.CategoryDto;
import mate.academy.bookstoreapp.dto.category.CreateCategoryRequestDto;
import mate.academy.bookstoreapp.utils.StarterCategory;
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
class CategoryControllerTest {
    private static final Logger LOGGER = Logger.getLogger(
            CategoryControllerTest.class.getSimpleName());
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
    @Sql(scripts = {"classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            "classpath:databasescripts/books/delete-2nd-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Create category - Admin User")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createCategory_AdminUser_ReturnsCreatedCategory() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Horror",
                "Spooky description"
        );

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        LOGGER.info("Response content: " + content);
        CategoryDto createdCategory = objectMapper.readValue(content, CategoryDto.class);

        assertNotNull(createdCategory);
        assertEquals("Horror", createdCategory.name());
        assertEquals("Spooky description", createdCategory.description());
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get a category by ID - Valid ID")
    public void getCategoryById_ValidId_ReturnsCategoryDto() throws Exception {
        Long validId = 1L;

        MvcResult result = mockMvc.perform(get("/categories/{id}", validId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        LOGGER.info("Response content: " + content);

        CategoryDto category = objectMapper.readValue(content, CategoryDto.class);

        assertNotNull(category);
        assertEquals(validId, category.id());
        assertEquals(StarterCategory.NAME, category.name());
        assertEquals(StarterCategory.DESCRIPTION, category.description());
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all categories with pagination")
    public void findAll_ReturnsPageOfCategories() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        LOGGER.info("Response content: " + content);

        List<CategoryDto> categories = objectMapper.readValue(content,
                new TypeReference<List<CategoryDto>>() {});

        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertEquals(StarterCategory.NAME, categories.get(0).name());
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Update category - Admin User")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateCategory_AdminUser_ReturnsUpdatedCategory() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Melodrama",
                "Very boring description"
        );

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        LOGGER.info("Response content: " + content);
        CategoryDto updatedCategory = objectMapper.readValue(content, CategoryDto.class);

        assertNotNull(updatedCategory);
        assertEquals("Melodrama", updatedCategory.name());
        assertEquals("Very boring description", updatedCategory.description());
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Delete category - Admin User")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteCategory_AdminUser_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Sql(scripts = "classpath:databasescripts/books/add-books-and-categories"
            + "-to-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:databasescripts/books/delete-books-and-categories"
            + "-from-books-and-categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get books by category ID - Valid Category ID")
    public void getBooksByCategoryId_ValidCategoryId_ReturnsBooksList() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        LOGGER.info("Response content: " + content);

        List<BookDtoWithoutCategoryIds> books = objectMapper.readValue(content,
                new TypeReference<List<BookDtoWithoutCategoryIds>>() {});

        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
    }
}
