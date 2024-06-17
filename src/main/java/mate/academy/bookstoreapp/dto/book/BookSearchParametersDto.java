package mate.academy.bookstoreapp.dto.book;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] priceRanges
) {
}
