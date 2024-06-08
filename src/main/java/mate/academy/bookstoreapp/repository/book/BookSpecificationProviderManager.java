package mate.academy.bookstoreapp.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.model.Book;
import mate.academy.bookstoreapp.repository.SpecificationProvider;
import mate.academy.bookstoreapp.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No specification provider found for key: " + key));
    }
}
