package it.lorenzopaciello.awesomepizza.unit.utils;

import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.model.dto.request.OrderDto;

import java.util.List;


public class OrderUtils {

    public static Order getOrderSingleEntity(){
        return Order.builder()
                .id(1L)
                .code("EXAMPLE_CODE")
                .build();
    }

    public static OrderDto getValidOrder1PizzaDto(){
        return OrderDto.builder()
                .name("Mario")
                .lastName("Rossi")
                .email("provaEmail@gmail.com")
                .phone("3334355555")
                .order(List.of(OrderDto.PizzaOrderDto.builder()
                                .idPizza(1L)
                                .quantity(5)
                        .build()))
                .build();
    }

    public static OrderDto getValidOrder2PizzaDto(){
        return OrderDto.builder()
                .name("Mario")
                .lastName("Rossi")
                .email("provaEmail@gmail.com")
                .phone("3334355555")
                .order(List.of(
                        OrderDto.PizzaOrderDto.builder()
                            .idPizza(1L)
                            .quantity(5)
                            .build(),
                        OrderDto.PizzaOrderDto.builder()
                            .idPizza(2L)
                            .quantity(4)
                            .build())
                        )
                .build();
    }

    public static OrderDto getValidOrder3Pizza1NotExistDto(){
        return OrderDto.builder()
                .name("Mario")
                .lastName("Rossi")
                .email("provaEmail@gmail.com")
                .phone("3334355555")
                .order(List.of(
                        OrderDto.PizzaOrderDto.builder()
                                .idPizza(1L)
                                .quantity(5)
                                .build(),
                        OrderDto.PizzaOrderDto.builder()
                                .idPizza(2L)
                                .quantity(4)
                                .build(),
                        OrderDto.PizzaOrderDto.builder()
                                .idPizza(2000L)
                                .quantity(4)
                                .build())
                )
                .build();
    }
}
