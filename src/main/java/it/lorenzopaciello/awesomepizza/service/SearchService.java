package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.exception.custom.NotFoundException;
import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.repository.PizzaRepository;
import it.lorenzopaciello.awesomepizza.service.interfaces.SearchServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService implements SearchServiceInterface {

    private PizzaRepository pizzaRepository;

    @Autowired
    public SearchService(PizzaRepository pizzaRepository){
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public Pizza findPizzaById(Long idPizza) {
        return this.pizzaRepository.findByIdAndAvailableIsTrue(idPizza).orElseThrow(
                () -> new NotFoundException("NotFound.pizza.id"));
    }
}
