package mate.academy.bookstoreapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import mate.academy.bookstoreapp.dto.category.CategoryDto;
import mate.academy.bookstoreapp.dto.category.CreateCategoryRequestDto;
import mate.academy.bookstoreapp.mapper.CategoryMapper;
import mate.academy.bookstoreapp.model.Category;
import mate.academy.bookstoreapp.repository.category.CategoryRepository;
import mate.academy.bookstoreapp.service.impl.CategoryServiceImpl;
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
public class CategoryServiceTest {
    private static final Logger LOGGER = Logger.getLogger(
            CategoryServiceTest.class.getSimpleName());

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CreateCategoryRequestDto requestDto;
    private CategoryDto categoryDto;
    private Category category;

    @BeforeEach
    public void setUpCategoryDto() {
        requestDto = new CreateCategoryRequestDto(
                StarterCategory.NAME,
                StarterCategory.DESCRIPTION
        );

        category = new Category();
        category.setId(1L);
        category.setName(StarterCategory.NAME);
        category.setDescription(StarterCategory.DESCRIPTION);

        categoryDto = new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    @AfterEach
    public void tearDown() {
        requestDto = null;
        categoryDto = null;
        category = null;
    }

    @DisplayName("Create new category")
    @Test
    public void save_valid_CreateCategoryRequestDto_returns_CategoryDto() {
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto expected = categoryDto;
        CategoryDto actual = categoryService.save(requestDto);
        LOGGER.info(actual.toString());

        assertEquals(expected, actual);

        verify(categoryMapper).toModel(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @DisplayName("Get all Categories - With pagination")
    @Test
    public void findAll_returns_ListOfCategoryDto() {
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Thriller");
        category2.setDescription("Spooky description");

        CategoryDto categoryDto2 = new CategoryDto(
                category2.getId(),
                category2.getName(),
                category2.getDescription()
        );

        Pageable pageable = PageRequest.of(0, 5);
        List<Category> categories = List.of(category, category2);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toListDto(categoryPage.getContent()))
                .thenReturn(List.of(categoryDto, categoryDto2));

        List<CategoryDto> expected = List.of(categoryDto, categoryDto2);
        List<CategoryDto> actual = categoryService.findAll(pageable).stream()
                .peek(s -> LOGGER.info("Category: " + s.toString()))
                .toList();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @DisplayName("Get Category by ID - Valid ID")
    @Test
    public void findCategory_ByValidId_returns_CategoryDto() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto expected = categoryDto;
        CategoryDto actual = categoryService.getById(categoryId);
        LOGGER.info(actual.toString());

        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
    }

    @DisplayName("Get Category by ID - Non existing ID - throws exception")
    @Test
    public void findCategory_ByInvalidId_throwsEntityNotFoundException() {
        Long categoryId = 2L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(categoryId);
        });

        assertEquals("Can't find the category by id: " + categoryId, exception.getMessage());

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper, never()).toDto(any());
    }

    @DisplayName("Update category - Returns updated category")
    @Test
    public void valid_update_with_CreateCategoryRequestDto() {
        CreateCategoryRequestDto newRequestDto = new CreateCategoryRequestDto(
                "Drama",
                "Sad, sad description"
        );
        CategoryDto updatedDto = new CategoryDto(
                1L,
                "Drama",
                "Sad, sad description"
        );
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategoryFromDto(newRequestDto, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(updatedDto);

        CategoryDto result = categoryService.update(categoryId, newRequestDto);
        LOGGER.info("Updated CategoryDto: " + result);

        assertEquals(updatedDto, result);
        assertEquals("Drama", result.name());

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDto(newRequestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @DisplayName("Delete category")
    @Test
    public void deleteCategory_byValidId() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(categoryId);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        categoryService.deleteById(categoryId);

        assertEquals(Optional.empty(), categoryRepository.findById(categoryId));

        verify(categoryRepository).deleteById(categoryId);
    }
}
