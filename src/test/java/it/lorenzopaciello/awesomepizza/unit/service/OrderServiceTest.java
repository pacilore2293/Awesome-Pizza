package it.lorenzopaciello.awesomepizza.unit.service;

import it.lorenzopaciello.awesomepizza.exception.custom.NotFoundException;
import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.controller.dto.request.OrderRequestDto;
import it.lorenzopaciello.awesomepizza.repository.OrderRepository;
import it.lorenzopaciello.awesomepizza.service.OrderService;
import it.lorenzopaciello.awesomepizza.service.SearchService;
import it.lorenzopaciello.awesomepizza.unit.utils.OrderUtils;
import it.lorenzopaciello.awesomepizza.unit.utils.PizzaUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private SearchService searchService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Nested
    @DisplayName("Test per il metodo createOrder successo")
    class createOrderTestsSuccess {

        @Test
        @DisplayName("Dovrebbe restituire l'ordine creato - 1 pizza")
        void shouldReturnOrderCode_whenAllPizzasAvailable() {

            OrderRequestDto requestBody = OrderUtils.getValidOrder1PizzaDto();

            when(searchService.findPizzaById(requestBody.getOrder().get(0).getIdPizza())).thenReturn(PizzaUtils.getSinglePizzaId1());
            when(orderRepository.save(any())).thenReturn(OrderUtils.getOrderSingleEntity());

            Order order = orderService.createOrder(requestBody);

            assertNotNull(order);

            verify(searchService, times(1)).findPizzaById(requestBody.getOrder().get(0).getIdPizza());
            verify(orderRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("Dovrebbe restituire l'ordine creato - 2 pizze")
        void shouldReturnOrderCode_when2PizzasAvailable() {

            OrderRequestDto requestBody = OrderUtils.getValidOrder2PizzaDto();

            when(searchService.findPizzaById(requestBody.getOrder().get(0).getIdPizza())).thenReturn(PizzaUtils.getSinglePizzaId1());
            when(searchService.findPizzaById(requestBody.getOrder().get(1).getIdPizza())).thenReturn(PizzaUtils.getSinglePizzaId2());
            when(orderRepository.save(any())).thenReturn(OrderUtils.getOrderSingleEntity());

            Order order = orderService.createOrder(requestBody);

            assertNotNull(order);

            verify(searchService, times(1)).findPizzaById(requestBody.getOrder().get(0).getIdPizza());
            verify(searchService, times(1)).findPizzaById(requestBody.getOrder().get(1).getIdPizza());
            verify(orderRepository, times(1)).save(any());
        }
    }

    @Nested
    @DisplayName("Test per il metodo createOrder errore")
    class createOrderTestsFailed {

        @Test
        @DisplayName("Dovrebbe sollevare NotFoundExcpetion")
        void shouldReturnOrderCode_whenAllPizzasAvailable() {

            OrderRequestDto requestBody = OrderUtils.getValidOrder3Pizza1NotExistDto();

            when(searchService.findPizzaById(requestBody.getOrder().get(0).getIdPizza())).thenReturn(PizzaUtils.getSinglePizzaId1());
            when(searchService.findPizzaById(requestBody.getOrder().get(1).getIdPizza())).thenReturn(PizzaUtils.getSinglePizzaId2());
            when(searchService.findPizzaById(requestBody.getOrder().get(2).getIdPizza())).thenThrow(new NotFoundException("Pizza non trovata"));

            assertThrows(NotFoundException.class, () -> orderService.createOrder(requestBody));

            verify(searchService, times(1)).findPizzaById(requestBody.getOrder().get(0).getIdPizza());
            verify(searchService, times(1)).findPizzaById(requestBody.getOrder().get(1).getIdPizza());
            verify(orderRepository, times(0)).save(any());

        }
    }

}
