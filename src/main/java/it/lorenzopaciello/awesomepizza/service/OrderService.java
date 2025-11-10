package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.controller.dto.request.OrderRequestDto;
import it.lorenzopaciello.awesomepizza.repository.OrderRepository;
import it.lorenzopaciello.awesomepizza.service.interfaces.OrderServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements OrderServiceInterface {

    private SearchService searchService;
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(SearchService searchService,
                        OrderRepository orderRepository){
        this.searchService = searchService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(OrderRequestDto orderRequestDto) {
        Order newOrder = new Order();
        orderRequestDto.getOrder().forEach(order -> {
            Pizza pizza = this.searchService.findPizzaById(order.getIdPizza());
            newOrder.addPizza(pizza, order.getQuantity());
        });
        newOrder.createGuestUser(orderRequestDto.getName(), orderRequestDto.getLastName(), orderRequestDto.getEmail(), orderRequestDto.getPhone());
        return this.orderRepository.save(newOrder);
    }
}
