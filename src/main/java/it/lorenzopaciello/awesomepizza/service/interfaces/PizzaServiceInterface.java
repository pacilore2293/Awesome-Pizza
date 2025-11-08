package it.lorenzopaciello.awesomepizza.service.interfaces;

import it.lorenzopaciello.awesomepizza.model.projection.PizzaProjection;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PizzaServiceInterface {
    public List<PizzaProjection> getAllPizzas(String language);
}
