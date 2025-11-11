package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import it.lorenzopaciello.awesomepizza.exception.custom.ConflictException;
import it.lorenzopaciello.awesomepizza.exception.custom.NotFoundException;
import it.lorenzopaciello.awesomepizza.model.*;
import it.lorenzopaciello.awesomepizza.model.enums.OrderActionEnum;
import it.lorenzopaciello.awesomepizza.repository.OrderRepository;
import it.lorenzopaciello.awesomepizza.repository.PizzaRepository;
import it.lorenzopaciello.awesomepizza.repository.RefreshTokenRepository;
import it.lorenzopaciello.awesomepizza.repository.RoleRepository;
import it.lorenzopaciello.awesomepizza.service.interfaces.SearchServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SearchService implements SearchServiceInterface {

    private PizzaRepository pizzaRepository;
    private RoleRepository roleRepository;
    private OrderRepository orderRepository;
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public SearchService(PizzaRepository pizzaRepository,
                         RoleRepository roleRepository,
                         OrderRepository orderRepository,
                         RefreshTokenRepository refreshTokenRepository){
        this.pizzaRepository = pizzaRepository;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Pizza findPizzaById(Long idPizza) {
        return this.pizzaRepository.findByIdAndAvailableIsTrue(idPizza).orElseThrow(
                () -> new NotFoundException(ErrorCode.PIZZA_NOT_FOUND_ID));
    }

    @Override
    public Role findRoleByName(String name) {
        return this.roleRepository.findByName(name).orElseThrow(
                () -> new NotFoundException(ErrorCode.ROLE_NOT_FOUND_NAME));
    }

    @Override
    public Order findOrderByCode(String orderCode) {
        return this.orderRepository.findByCode(orderCode).orElseThrow(
                () -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_ID));
    }

    @Override
    public Order findOrderReadyForTaken(Long idOrder) {
        return orderRepository.findByIdAndStatus(idOrder, OrderActionEnum.IN_ATTESA)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_TAKEN));
    }

    @Override
    public Order findOrderReadyForReady(Long idOrder, String usernameOperator) {
        Optional<Order> orderOptional = orderRepository.findByIdAndStatus(idOrder, OrderActionEnum.PRESO_IN_CARICO);
        orderOptional.orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_READY));

        OrderAction lastAction = orderOptional.get().getOrderActions().iterator().next();

        //Se l'operatore non e quello che ha iniziato la presa in carico, errore
        if(!usernameOperator.equalsIgnoreCase(lastAction.getOperator().getUsername())){
            throw new ConflictException(ErrorCode.ORDER_ACCESS_DENIED_READY);
        }else{
            return orderOptional.get();
        }
    }

    @Override
    public Order findOrderReadyForEscape(Long idOrder) {
        return orderRepository.findByIdAndStatus(idOrder, OrderActionEnum.PRONTO_PER_LA_CONSEGNA)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_ESCAPE));
    }

    @Override
    public RefreshToken findRefreshTokenByToken(String token) {
        return this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(ErrorCode.AUTH_REFRESH_TOKEN_NOT_FOUND));
    }
}
