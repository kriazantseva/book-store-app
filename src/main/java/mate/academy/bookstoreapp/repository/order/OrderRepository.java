package mate.academy.bookstoreapp.repository.order;

import java.util.Optional;
import mate.academy.bookstoreapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.user.id = :userId")
    Optional<Order> findByUserId(@Param("userId") Long userId);

    @Query("select o from Order o where o.user.id = :userId and o.id = :orderId")
    Optional<Order> findByUserIdAndOrderId(@Param("userId") Long userId,
                                           @Param("orderId") Long orderId);
}
