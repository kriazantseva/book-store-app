package mate.academy.bookstoreapp.repository.orderitem;

import mate.academy.bookstoreapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
