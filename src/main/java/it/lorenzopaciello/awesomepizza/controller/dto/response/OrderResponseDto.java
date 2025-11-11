package it.lorenzopaciello.awesomepizza.controller.dto.response;

import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.model.PizzaOrder;
import it.lorenzopaciello.awesomepizza.model.enums.OrderActionEnum;
import lombok.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;
    private String nameGuest;
    private String lastNameGuest;
    private String orderCode;
    private Double totalPrice = 0.0;
    private OrderActionEnum status;

    List<OrderDetail> details = new ArrayList<>();

    public OrderResponseDto(Order order){
        this.id = order.getId();
        this.nameGuest = order.getGuest().getName();
        this.lastNameGuest = order.getGuest().getLastName();
        this.orderCode = order.getCode();
        this.status = order.getStatus();

        if(order.getPizzaOrders() != null && !order.getPizzaOrders().isEmpty()){
            for(PizzaOrder pizzaOrder : order.getPizzaOrders()){
                this.totalPrice += pizzaOrder.getPizza().getPrice() * pizzaOrder.getQuantity();
                    OrderDetail orderDetail = OrderDetail.builder()
                            .namePizza(LocaleContextHolder.getLocale().getLanguage().equalsIgnoreCase("en") ? pizzaOrder.getPizza().getNameEng() : pizzaOrder.getPizza().getNameIta())
                            .price(pizzaOrder.getPizza().getPrice())
                            .quantity(pizzaOrder.getQuantity())
                            .build();
                    this.details.add(orderDetail);
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderDetail{
        private String namePizza;
        private Double price;
        private Integer quantity;
    }
}