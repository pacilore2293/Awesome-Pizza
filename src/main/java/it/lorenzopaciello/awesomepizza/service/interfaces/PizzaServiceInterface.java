package it.lorenzopaciello.awesomepizza.service.interfaces;

import it.lorenzopaciello.awesomepizza.controller.dto.response.PizzaResponseDto;

import java.util.List;

public interface PizzaServiceInterface {
    public List<PizzaResponseDto> getAllPizzas();
}
