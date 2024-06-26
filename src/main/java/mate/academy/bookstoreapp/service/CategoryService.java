package mate.academy.bookstoreapp.service;

import java.util.List;
import mate.academy.bookstoreapp.dto.category.CategoryDto;
import mate.academy.bookstoreapp.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CreateCategoryRequestDto requestDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
