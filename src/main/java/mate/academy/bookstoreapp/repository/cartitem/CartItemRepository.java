package mate.academy.bookstoreapp.repository.cartitem;

import mate.academy.bookstoreapp.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
