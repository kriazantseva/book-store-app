package mate.academy.bookstoreapp.repository.book;

import java.math.BigDecimal;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final int PRICE_MIN = 0;
    private static final int PRICE_MAX = 1;
    private static final String PRICE = "price";

    @Override
    public String getKey() {
        return "price";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            if (params.length == 1) {
                BigDecimal price = new BigDecimal(params[PRICE_MIN]);
                return criteriaBuilder.equal(root.get(PRICE), price);
            } else if (params.length == 2) {
                BigDecimal minPrice = new BigDecimal(params[PRICE_MIN]);
                BigDecimal maxPrice = new BigDecimal(params[PRICE_MAX]);
                return criteriaBuilder.between(root.get(PRICE), minPrice, maxPrice);
            } else {
                throw new IllegalArgumentException("Invalid price parameters");
            }
        };
    }
}
