package it.lorenzopaciello.awesomepizza.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pizza")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ita", nullable = false)
    private String nameIta;

    @Column(name = "name_eng", nullable = false)
    private String nameEng;

    @Column(name = "description_ita", nullable = false)
    private String descriptionIta;

    @Column(name = "description_eng", nullable = false)
    private String descriptionEng;

    @Column(name = "price")
    private Double price;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<PizzaIngredient> pizzaIngredients = new HashSet<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Aggiunge un ingrediente alla pizza, creando l'entità ponte.
     */
    public void addIngredient(Ingredient ingredient, Double quantity) {
        PizzaIngredient pizzaIngredient = PizzaIngredient.builder()
                .pizza(this)
                .ingredient(ingredient)
                .quantity(quantity)
                .build();
        this.pizzaIngredients.add(pizzaIngredient);
    }

    /**
     * Rimuove un ingrediente dalla pizza, eliminando anche l'entità ponte.
     */
    public void removeIngredient(Ingredient ingredient) {
        this.pizzaIngredients.removeIf(pizzaIngredient -> {
            boolean match = pizzaIngredient.getIngredient().equals(ingredient);
            if (match) {
                pizzaIngredient.setPizza(null);
                pizzaIngredient.setIngredient(null);
            }
            return match;
        });
    }
}
