package it.lorenzopaciello.awesomepizza.service.interfaces;


import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.model.dto.request.OrderDto;

public interface OrderServiceInterface {
    public Order createOrder(OrderDto orderDto);
}
