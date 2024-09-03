package mate.academy.bookstoreapp.repository.shoppingcart;

import java.util.Optional;
import mate.academy.bookstoreapp.model.ShoppingCart;
import mate.academy.bookstoreapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findShoppingCartByUser(User user);
}
