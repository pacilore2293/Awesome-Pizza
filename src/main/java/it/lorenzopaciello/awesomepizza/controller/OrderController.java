package it.lorenzopaciello.awesomepizza.controller;

import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.model.dto.request.OrderDto;
import it.lorenzopaciello.awesomepizza.model.projection.PizzaProjection;
import it.lorenzopaciello.awesomepizza.service.OrderService;
import it.lorenzopaciello.awesomepizza.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController("orderController")
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderDto order, Locale locale) {
        return ResponseEntity.ok(this.orderService.createOrder(order));
    }
}
