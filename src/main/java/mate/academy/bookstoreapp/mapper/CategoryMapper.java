package mate.academy.bookstoreapp.mapper;

import java.util.List;
import mate.academy.bookstoreapp.config.MapperConfig;
import mate.academy.bookstoreapp.dto.category.CategoryDto;
import mate.academy.bookstoreapp.dto.category.CreateCategoryRequestDto;
import mate.academy.bookstoreapp.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    List<CategoryDto> toListDto(List<Category> categories);

    Category toModel(CreateCategoryRequestDto requestDto);

    void updateCategoryFromDto(CreateCategoryRequestDto requestDto, @MappingTarget Category entity);
}
