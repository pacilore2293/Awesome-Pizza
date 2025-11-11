package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.controller.dto.response.OrderResponseDto;
import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import it.lorenzopaciello.awesomepizza.exception.custom.ConflictException;
import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.model.OrderAction;
import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.controller.dto.request.OrderRequestDto;
import it.lorenzopaciello.awesomepizza.model.User;
import it.lorenzopaciello.awesomepizza.model.enums.OrderActionEnum;
import it.lorenzopaciello.awesomepizza.repository.OrderActionRepository;
import it.lorenzopaciello.awesomepizza.repository.OrderRepository;
import it.lorenzopaciello.awesomepizza.service.interfaces.OrderServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements OrderServiceInterface {

    private UserDetailsService userDetailsService;
    private SearchService searchService;
    private OrderRepository orderRepository;
    private JwtService jwtService;
    private OrderActionRepository orderActionRepository;

    @Autowired
    public OrderService(SearchService searchService,
                        OrderRepository orderRepository,
                        JwtService jwtService,
                        OrderActionRepository orderActionRepository,
                        UserDetailsService userDetailsService){
        this.searchService = searchService;
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.orderActionRepository = orderActionRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order newOrder = new Order();
        orderRequestDto.getOrder().forEach(order -> {
            Pizza pizza = this.searchService.findPizzaById(order.getIdPizza());
            newOrder.addPizza(pizza, order.getQuantity());
        });
        newOrder.createGuestUser(orderRequestDto.getName(), orderRequestDto.getLastName(), orderRequestDto.getEmail(), orderRequestDto.getPhone());
        newOrder.setStatus(OrderActionEnum.IN_ATTESA);
        Order order = this.orderRepository.save(newOrder);
        return new OrderResponseDto(order);
    }

    @Override
    public OrderResponseDto orderDetails(String orderCode) {
        Order order = this.searchService.findOrderByCode(orderCode);
        return new OrderResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> searchStatus(OrderActionEnum searchStatus) {
        List<Order> orders = new ArrayList<>();
        if(searchStatus == null){
            orders = this.orderRepository.findAll();
        }else{
            orders = this.orderRepository.findByStatus(searchStatus);
        }
        return orders.stream().map(OrderResponseDto::new).toList();
    }

    @Override
    public OrderResponseDto taken(Long idOrder, HttpServletRequest request) {
        String token = this.jwtService.extractBearerToken(request);
        String username = this.jwtService.extractUsername(token);

        Order order = this.searchService.findOrderReadyForTaken(idOrder);

        //Verifica se l'operatore attuale ha altre prese in carico
        List<OrderAction> orderTakensOperatorNotComplete = this.orderActionRepository.findByOperator_usernameAndActionAndIsCompleteFalse(username, OrderActionEnum.PRESO_IN_CARICO);
        if(!orderTakensOperatorNotComplete.isEmpty()){
            throw new ConflictException(ErrorCode.USER_ALREADY_HAS_TAKEN);
        }

        User operator = (User) this.userDetailsService.loadUserByUsername(username);

        order.getOrderActions().add(OrderAction.builder()
                        .order(order)
                        .action(OrderActionEnum.PRESO_IN_CARICO)
                        .operator(operator)
                .build());
        order.setStatus(OrderActionEnum.PRESO_IN_CARICO);
        Order savedOrder = this.orderRepository.save(order);
        return new OrderResponseDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponseDto ready(Long idOrder, HttpServletRequest request) {
        String token = this.jwtService.extractBearerToken(request);
        String username = this.jwtService.extractUsername(token);

        //Deve chiudere la presa in carico chi ha iniziato
        Order order = this.searchService.findOrderReadyForReady(idOrder, username);
        User operator = (User) this.userDetailsService.loadUserByUsername(username);

        OrderAction lastAction = order.getOrderActions().iterator().next();
        lastAction.setComplete(true);
        this.orderActionRepository.save(lastAction);

        order.getOrderActions().add(OrderAction.builder()
                .order(order)
                .action(OrderActionEnum.PRONTO_PER_LA_CONSEGNA)
                .operator(operator)
                .build());
        order.setStatus(OrderActionEnum.PRONTO_PER_LA_CONSEGNA);
        Order savedOrder = this.orderRepository.save(order);
        return new OrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto escape(Long idOrder, HttpServletRequest request) {
        String token = this.jwtService.extractBearerToken(request);
        String username = this.jwtService.extractUsername(token);

        Order order = this.searchService.findOrderReadyForEscape(idOrder);
        User operator = (User) this.userDetailsService.loadUserByUsername(username);

        OrderAction lastAction = order.getOrderActions().iterator().next();
        lastAction.setComplete(true);
        this.orderActionRepository.save(lastAction);

        order.getOrderActions().add(OrderAction.builder()
                .order(order)
                .action(OrderActionEnum.EVASO)
                .operator(operator)
                .build());
        order.setStatus(OrderActionEnum.EVASO);
        Order savedOrder = this.orderRepository.save(order);
        return new OrderResponseDto(savedOrder);
    }
}
