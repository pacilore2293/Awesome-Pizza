package it.lorenzopaciello.awesomepizza.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "pizza_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PizzaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_pizza_id", nullable = false)
    private Pizza pizza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_order_id", nullable = false)
    private Order order;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}

