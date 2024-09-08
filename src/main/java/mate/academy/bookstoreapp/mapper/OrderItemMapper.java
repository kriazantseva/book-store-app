package mate.academy.bookstoreapp.mapper;

import java.util.Set;
import mate.academy.bookstoreapp.config.MapperConfig;
import mate.academy.bookstoreapp.dto.orderitem.CreateOrderItemRequestDto;
import mate.academy.bookstoreapp.dto.orderitem.OrderItemDto;
import mate.academy.bookstoreapp.exceptions.EntityNotFoundException;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.model.OrderItem;
import mate.academy.bookstoreapp.repository.book.BookRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "bookPrice", source = "book.price")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromIdForOrder")
    OrderItem toEntity(CreateOrderItemRequestDto requestDto,
                       @Context BookRepository bookRepository);

    Set<OrderItemDto> toDtoSet(Set<OrderItem> orderItemList);

    @Named("bookFromIdForOrder")
    default Book bookFromIdForOrder(Long id, @Context BookRepository bookRepository) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found")
        );
    }
}
