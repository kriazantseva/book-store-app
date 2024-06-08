package mate.academy.bookstoreapp.dto;

public record BookSearchParametersDto(String[] titles,
                                      String[] authors,
                                      String[] priceRanges) {

}
