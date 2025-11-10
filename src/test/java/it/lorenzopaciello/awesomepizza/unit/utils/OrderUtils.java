package it.lorenzopaciello.awesomepizza.unit.utils;

import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.controller.dto.request.OrderRequestDto;

import java.util.List;


public class OrderUtils {

    public static Order getOrderSingleEntity(){
        return Order.builder()
                .id(1L)
                .code("EXAMPLE_CODE")
                .build();
    }

    public static OrderRequestDto getValidOrder1PizzaDto(){
        return OrderRequestDto.builder()
                .name("Mario")
                .lastName("Rossi")
                .email("provaEmail@gmail.com")
                .phone("3334355555")
                .order(List.of(OrderRequestDto.PizzaOrderDto.builder()
                                .idPizza(1L)
                                .quantity(5)
                        .build()))
                .build();
    }

    public static OrderRequestDto getValidOrder2PizzaDto(){
        return OrderRequestDto.builder()
                .name("Mario")
                .lastName("Rossi")
                .email("provaEmail@gmail.com")
                .phone("3334355555")
                .order(List.of(
                        OrderRequestDto.PizzaOrderDto.builder()
                            .idPizza(1L)
                            .quantity(5)
                            .build(),
                        OrderRequestDto.PizzaOrderDto.builder()
                            .idPizza(2L)
                            .quantity(4)
                            .build())
                        )
                .build();
    }

    public static OrderRequestDto getValidOrder3Pizza1NotExistDto(){
        return OrderRequestDto.builder()
                .name("Mario")
                .lastName("Rossi")
                .email("provaEmail@gmail.com")
                .phone("3334355555")
                .order(List.of(
                        OrderRequestDto.PizzaOrderDto.builder()
                                .idPizza(1L)
                                .quantity(5)
                                .build(),
                        OrderRequestDto.PizzaOrderDto.builder()
                                .idPizza(2L)
                                .quantity(4)
                                .build(),
                        OrderRequestDto.PizzaOrderDto.builder()
                                .idPizza(2000L)
                                .quantity(4)
                                .build())
                )
                .build();
    }
}
