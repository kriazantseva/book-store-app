package mate.academy.bookstoreapp.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.bookstoreapp.config.MapperConfig;
import mate.academy.bookstoreapp.dto.book.BookDto;
import mate.academy.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreapp.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.model.Category;
import org.mapstruct.AfterMapping;
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

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            Set<Long> categoryIds = book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet());
        }
    }
}
