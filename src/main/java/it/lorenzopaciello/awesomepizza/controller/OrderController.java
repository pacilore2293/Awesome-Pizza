package it.lorenzopaciello.awesomepizza.controller;

import it.lorenzopaciello.awesomepizza.controller.dto.response.OrderResponseDto;
import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.controller.dto.request.OrderRequestDto;
import it.lorenzopaciello.awesomepizza.model.enums.OrderActionEnum;
import it.lorenzopaciello.awesomepizza.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.createOrder(orderRequestDto));
    }

    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> getDetailOrder(@RequestParam(value = "orderCode") String orderCode) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.orderDetails(orderCode));
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResponseDto>> searchOrders(@RequestParam(value = "status", required = false) OrderActionEnum searchStatus) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.searchStatus(searchStatus));
    }

    @PutMapping(value = "/taken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> takenOrder(@RequestParam(name = "idOrder") Long idOrder, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.taken(idOrder, request));
    }

    @PutMapping(value = "/ready", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> readyOrder(@RequestParam(name = "idOrder") Long idOrder, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.ready(idOrder, request));
    }

    @PutMapping(value = "/escape", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> escapeOrder(@RequestParam(name = "idOrder") Long idOrder, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.escape(idOrder, request));
    }

}
