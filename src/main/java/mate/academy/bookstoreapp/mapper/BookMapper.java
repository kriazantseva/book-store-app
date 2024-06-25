package mate.academy.bookstoreapp.mapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.bookstoreapp.config.MapperConfig;
import mate.academy.bookstoreapp.dto.book.BookDto;
import mate.academy.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreapp.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.model.Category;
import mate.academy.bookstoreapp.repository.category.CategoryRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    List<BookDto> toDtoList(List<Book> books);

    List<BookDtoWithoutCategoryIds> toDtoWithoutCategoriesList(List<Book> books);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book entity);

    default Set<Category> mapCategoryIdsToCategories(List<Long> categoryIds,
                                                     CategoryRepository categoryRepository) {
        return categoryIds.stream()
                .map(categoryRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void mapCategoryIds(@MappingTarget Book book, CreateBookRequestDto requestDto,
                                @Context CategoryRepository categoryRepository) {
        if (requestDto.categoryIds() != null) {
            book.setCategories(mapCategoryIdsToCategories(
                    requestDto.categoryIds(), categoryRepository
            ));
        }
    }
}
