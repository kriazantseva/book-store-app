package mate.academy.bookstoreapp.repository.category;

import java.util.Optional;
import mate.academy.bookstoreapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
