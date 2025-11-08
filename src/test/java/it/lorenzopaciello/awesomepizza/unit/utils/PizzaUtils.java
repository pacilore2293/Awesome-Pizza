package it.lorenzopaciello.awesomepizza.unit.utils;

import it.lorenzopaciello.awesomepizza.model.Pizza;

import java.time.Instant;
import java.util.List;

public class PizzaUtils {

    public static List<Pizza> getPizzaList(){
        return List.of(
                Pizza.builder()
                        .id(1L)
                        .nameIta("Margherita")
                        .nameEng("Margherita")
                        .descriptionIta("Classica pizza con pomodoro, mozzarella e basilico fresco.")
                        .descriptionEng("Classic pizza with tomato, mozzarella, and fresh basil.")
                        .price(6.00)
                        .available(true)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build(),
                Pizza.builder()
                        .id(2L)
                        .nameIta("Diavola")
                        .nameEng("Spicy Diavola")
                        .descriptionIta("Pomodoro, mozzarella e salame piccante per chi ama il gusto deciso.")
                        .descriptionEng("Tomato, mozzarella, and spicy salami for strong flavors lovers.")
                        .price(7.50)
                        .available(true)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build(),
                Pizza.builder()
                        .id(3L)
                        .nameIta("Capricciosa")
                        .nameEng("Capricciosa")
                        .descriptionIta("Pomodoro, mozzarella, prosciutto cotto, funghi, carciofi e olive nere.")
                        .descriptionEng("Tomato, mozzarella, cooked ham, mushrooms, artichokes, and black olives.")
                        .price(8.50)
                        .available(true)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );
    }

    public static Pizza getSinglePizzaId1(){
        return Pizza.builder()
                .id(1L)
                .nameIta("Margherita")
                .nameEng("Margherita")
                .descriptionIta("Classica pizza con pomodoro, mozzarella e basilico fresco.")
                .descriptionEng("Classic pizza with tomato, mozzarella, and fresh basil.")
                .price(6.00)
                .available(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public static Pizza getSinglePizzaId2(){
        return Pizza.builder()
                .id(2L)
                .nameIta("Diavola")
                .nameEng("Spicy Diavola")
                .descriptionIta("Pomodoro, mozzarella e salame piccante per chi ama il gusto deciso.")
                .descriptionEng("Classic pizza with tomato, mozzarella, and fresh basil.")
                .price(6.00)
                .available(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}
