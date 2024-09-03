package mate.academy.bookstoreapp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString(exclude = "orderItems")
@EqualsAndHashCode(exclude = {"orderItems", "status"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    private BigDecimal total;
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinTable(
            name = "orders_order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_item_id")
    )
    private Set<OrderItem> orderItems = new HashSet<>();

    public enum Status {
        PENDING,
        AWAITING_PAYMENT,
        AWAITING_FULFILLMENT,
        AWAITING_SHIPMENT,
        AWAITING_PICKUP,
        PARTIALLY_SHIPPED,
        COMPLETED,
        SHIPPED,
        CANCELLED,
        DECLINED,
        REFUNDED,
        DISPUTED,
        MANUAL_VERIFICATION_REQUIRED,
        PARTIALLY_REFUNDED
    }
}
