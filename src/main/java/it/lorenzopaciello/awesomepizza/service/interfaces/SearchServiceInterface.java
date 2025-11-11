package it.lorenzopaciello.awesomepizza.service.interfaces;

import it.lorenzopaciello.awesomepizza.model.Order;
import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.model.RefreshToken;
import it.lorenzopaciello.awesomepizza.model.Role;

public interface SearchServiceInterface {
    public Pizza findPizzaById(Long idPizza);
    public Role findRoleByName(String name);

    public Order findOrderByCode(String orderCode);
    public Order findOrderReadyForTaken(Long idOrder);
    public Order findOrderReadyForReady(Long idOrder, String usernameOperator);
    public Order findOrderReadyForEscape(Long idOrder);

    public RefreshToken findRefreshTokenByToken(String token);
}
