package mate.academy.bookstoreapp.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import mate.academy.bookstoreapp.validation.Isbn;

public record CreateBookRequestDto(
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank @Isbn String isbn,
        @NotNull @Positive BigDecimal price,
        @Size(max = 1000) String description,
        String coverImage
) {
}
