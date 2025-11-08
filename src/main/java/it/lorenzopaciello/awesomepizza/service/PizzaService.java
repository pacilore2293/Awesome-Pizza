package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.model.projection.PizzaProjection;
import it.lorenzopaciello.awesomepizza.repository.PizzaRepository;
import it.lorenzopaciello.awesomepizza.service.interfaces.PizzaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PizzaService implements PizzaServiceInterface {

    private PizzaRepository pizzaRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository){
        this.pizzaRepository = pizzaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PizzaProjection> getAllPizzas(String language) {
        return this.pizzaRepository.findAllByAvailableIsTrue().stream().map(pizza -> new PizzaProjection(pizza, language)).toList();
    }
}
