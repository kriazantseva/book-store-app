package mate.academy.bookstoreapp.mapper;

import java.util.List;
import mate.academy.bookstoreapp.config.MapperConfig;
import mate.academy.bookstoreapp.dto.BookDto;
import mate.academy.bookstoreapp.dto.CreateBookRequestDto;
import mate.academy.bookstoreapp.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    List<BookDto> toDtoList(List<Book> books);

    Book toModel(CreateBookRequestDto requestDto);
}
