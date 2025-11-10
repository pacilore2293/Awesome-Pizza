package it.lorenzopaciello.awesomepizza.service.interfaces;


import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.controller.dto.request.OrderRequestDto;

public interface OrderServiceInterface {
    public Order createOrder(OrderRequestDto orderRequestDto);
}
