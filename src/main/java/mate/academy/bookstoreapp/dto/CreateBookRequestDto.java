package mate.academy.bookstoreapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import mate.academy.bookstoreapp.validation.Isbn;

public record CreateBookRequestDto(
        @NotEmpty String title,
        @NotEmpty String author,
        @NotNull @Isbn String isbn,
        @NotNull @Positive BigDecimal price,
        @Size(max = 1000) String description,
        String coverImage
) {
}
