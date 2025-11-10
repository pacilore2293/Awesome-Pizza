package it.lorenzopaciello.awesomepizza.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
//@Check(constraints = "(fk_user_auth IS NOT NULL AND fk_user_guest IS NULL) OR (fk_user_auth IS NULL AND fk_user_guest IS NOT NULL)")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String code;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "fk_user_guest", unique = true)
    private UserGuest guest;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<PizzaOrder> pizzaOrders = new HashSet<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.code == null || this.code.isEmpty()) {
            this.code = generateOrderCode();
        }
    }

    /**
     * Aggiunge una pizza e la quantita all'ordine
     */
    public void addPizza(Pizza pizza, Integer quantity) {
        if (this.pizzaOrders == null) {
            this.pizzaOrders = new HashSet<>();
        }

        //Verifica se la pizza e gia stata inserita prima (gestione id duplicati più volte, in caso aumenta la quantità)
        PizzaOrder existingPizza = this.pizzaOrders.stream()
                .filter(po -> po.getPizza().getId().equals(pizza.getId()))
                .findFirst()
                .orElse(null);

        if (existingPizza != null) {
            this.pizzaOrders.remove(existingPizza);
            existingPizza.setQuantity(existingPizza.getQuantity() + quantity);
            this.pizzaOrders.add(existingPizza);
        } else {
            PizzaOrder pizzaOrder = PizzaOrder.builder()
                    .order(this)
                    .pizza(pizza)
                    .quantity(quantity)
                    .build();
            this.pizzaOrders.add(pizzaOrder);
        }
    }

    /**
     * Creazione utente client che ha indetto l'ordine
     */
    public void createGuestUser(String name, String lastName, String email, String phone) {
        this.guest = UserGuest.builder()
                .name(name)
                .lastName(lastName)
                .email(email)
                .telephone(phone)
                .build();
    }

    /**
     * Genera un codice ordine del tipo ORD-<YYYYMMDD>-<RANDOM>
     */
    private String generateOrderCode() {
        String datePart = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + datePart + "-" + randomPart;
    }
}
