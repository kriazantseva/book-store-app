package mate.academy.bookstoreapp.repository.orderitem;

import java.util.Optional;
import mate.academy.bookstoreapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("select oi from OrderItem oi where oi.id = :id and oi.order = :orderId "
            + "and oi.order.user.id = :userId")
    Optional<OrderItem> findByIdAndOrderIdAndUserId(@Param("id") Long id,
                                                    @Param("orderId") Long orderId,
                                                    @Param("userId") Long userId);
}
