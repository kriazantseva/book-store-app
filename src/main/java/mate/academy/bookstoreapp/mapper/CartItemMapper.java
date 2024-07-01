package mate.academy.bookstoreapp.mapper;

import mate.academy.bookstoreapp.config.MapperConfig;
import mate.academy.bookstoreapp.dto.cartitem.CartItemDto;
import mate.academy.bookstoreapp.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookstoreapp.exceptions.EntityNotFoundException;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.model.CartItem;
import mate.academy.bookstoreapp.repository.book.BookRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "bookPrice", source = "book.price")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toEntity(CreateCartItemRequestDto requestDto, @Context BookRepository bookRepository);

    void updateCartItemFromDto(CreateCartItemRequestDto requestDto,
                               @MappingTarget CartItem cartItem);

    @Named("bookFromId")
    default Book bookFromId(Long id, @Context BookRepository bookRepository) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found")
        );
    }
}
