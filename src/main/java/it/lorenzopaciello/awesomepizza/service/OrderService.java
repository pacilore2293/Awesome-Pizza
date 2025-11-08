package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.model.dto.request.OrderDto;
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
    public Order createOrder(OrderDto orderDto) {
        Order newOrder = new Order();
        orderDto.getOrder().forEach(order -> {
            Pizza pizza = this.searchService.findPizzaById(order.getIdPizza());
            newOrder.addPizza(pizza, order.getQuantity());
        });
        newOrder.createGuestUser(orderDto.getName(), orderDto.getLastName(), orderDto.getEmail(), orderDto.getPhone());
        return this.orderRepository.save(newOrder);
    }
}
