package mate.academy.bookstoreapp.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.BookSearchParametersDto;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.repository.SpecificationBuilder;
import mate.academy.bookstoreapp.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final int SINGLE_PRICE = 0;
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final String PRICE = "price";
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        if (searchParametersDto.authors() != null && searchParametersDto.authors().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AUTHOR)
                    .getSpecification(searchParametersDto.authors()));
        }
        if (searchParametersDto.titles() != null && searchParametersDto.titles().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TITLE)
                    .getSpecification(searchParametersDto.titles()));
        }
        if (searchParametersDto.priceRanges() != null) {
            for (String priceRange : searchParametersDto.priceRanges()) {
                String[] prices = priceRange.split("-");
                if (prices.length == 1) {
                    spec = spec.and(bookSpecificationProviderManager
                            .getSpecificationProvider(PRICE)
                            .getSpecification(new String[]{prices[SINGLE_PRICE]}));
                } else if (prices.length == 2) {
                    spec = spec.and(bookSpecificationProviderManager
                            .getSpecificationProvider(PRICE)
                            .getSpecification(prices));
                }
            }
        }
        return spec;
    }
}
