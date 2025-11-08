package it.lorenzopaciello.awesomepizza.service.interfaces;

import it.lorenzopaciello.awesomepizza.model.Pizza;

public interface SearchServiceInterface {
    public Pizza findPizzaById(Long idPizza);
}
