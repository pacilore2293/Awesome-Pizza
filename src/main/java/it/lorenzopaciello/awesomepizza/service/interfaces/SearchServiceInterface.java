package it.lorenzopaciello.awesomepizza.service.interfaces;

import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.model.Role;

public interface SearchServiceInterface {
    public Pizza findPizzaById(Long idPizza);
    public Role findRoleByName(String name);
}
