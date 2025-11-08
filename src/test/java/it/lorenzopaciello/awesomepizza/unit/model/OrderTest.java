package it.lorenzopaciello.awesomepizza.unit.model;

import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.model.PizzaOrder;
import it.lorenzopaciello.awesomepizza.unit.utils.PizzaUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    @Nested
    @DisplayName("Test per il metodo addPizza successo")
    class createOrderTestsSuccess {

        @Test
        @DisplayName("Dovrebbe aggiungere una nuova pizza se non esiste già nell'ordine")
        void shouldAddNewPizzaWhenNotExists() {

            Order order = new Order();
            Pizza pizza = PizzaUtils.getSinglePizzaId1();

            order.addPizza(pizza, 2);

            assertThat(order.getPizzaOrders()).hasSize(1);
            PizzaOrder po = order.getPizzaOrders().iterator().next();
            assertThat(po.getPizza().getId()).isEqualTo(1L);
            assertThat(po.getQuantity()).isEqualTo(2);
        }

        @Test
        @DisplayName("Dovrebbe sommare la quantità se la pizza è già presente nell'ordine")
        void shouldIncreaseQuantityWhenPizzaAlreadyExists() {

            Pizza pizza = PizzaUtils.getSinglePizzaId1();
            Order order = new Order();

            order.addPizza(pizza, 2);
            order.addPizza(pizza, 3);

            assertThat(order.getPizzaOrders()).hasSize(1);
            PizzaOrder po = order.getPizzaOrders().iterator().next();
            assertThat(po.getQuantity()).isEqualTo(5);
        }

        @Test
        @DisplayName("Dovrebbe aggiungere più pizze diverse all'ordine")
        void shouldAddMultipleDifferentPizzas() {

            Pizza margherita = PizzaUtils.getSinglePizzaId1();
            Pizza diavola = PizzaUtils.getSinglePizzaId2();

            Order order = new Order();

            order.addPizza(margherita, 2);
            order.addPizza(diavola, 1);

            assertThat(order.getPizzaOrders()).hasSize(2);
            assertThat(order.getPizzaOrders())
                    .extracting(po -> po.getPizza().getId())
                    .containsExactlyInAnyOrder(1L, 2L);
        }

    }

}
