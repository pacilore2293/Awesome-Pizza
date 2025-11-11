package it.lorenzopaciello.awesomepizza.service.interfaces;


import it.lorenzopaciello.awesomepizza.controller.dto.response.OrderResponseDto;
import it.lorenzopaciello.awesomepizza.controller.dto.request.OrderRequestDto;
import it.lorenzopaciello.awesomepizza.model.enums.OrderActionEnum;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderServiceInterface {
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    public OrderResponseDto orderDetails(String orderCode);
    public List<OrderResponseDto> searchStatus(OrderActionEnum searchStatus);
    public OrderResponseDto taken(Long idOrder, HttpServletRequest request);
    public OrderResponseDto ready(Long idOrder, HttpServletRequest request);
    public OrderResponseDto escape(Long idOrder, HttpServletRequest request);
}
