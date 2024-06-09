package mate.academy.bookstoreapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import mate.academy.bookstoreapp.validation.Isbn;

public record CreateBookRequestDto(
        @NotNull String title,
        @NotNull String author,
        @NotNull @Isbn String isbn,
        @NotNull @Min(0) BigDecimal price,
        String description,
        String coverImage
) {
}
