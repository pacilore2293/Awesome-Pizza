package it.lorenzopaciello.awesomepizza.controller;

import it.lorenzopaciello.awesomepizza.model.projection.PizzaProjection;
import it.lorenzopaciello.awesomepizza.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController("needController")
@RequestMapping("/api")
public class PizzaController {

    private PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService){
        this.pizzaService = pizzaService;
    }

    @GetMapping(value = "/pizzas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PizzaProjection>> getAllPizzas(Locale locale) {
        return ResponseEntity.ok(pizzaService.getAllPizzas(locale.getLanguage()));
    }
}
