package mate.academy.bookstoreapp.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotBlank(message = "Name is required") @NotNull String name,
        @Size(max = 1000) String description
) {
}
