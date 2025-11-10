package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.controller.dto.response.PizzaResponseDto;
import it.lorenzopaciello.awesomepizza.repository.PizzaRepository;
import it.lorenzopaciello.awesomepizza.service.interfaces.PizzaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class PizzaService implements PizzaServiceInterface {

    private PizzaRepository pizzaRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository){
        this.pizzaRepository = pizzaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PizzaResponseDto> getAllPizzas() {
        Locale locale = LocaleContextHolder.getLocale();
        return this.pizzaRepository.findAllByAvailableIsTrue().stream().map(pizza -> new PizzaResponseDto(pizza, locale.getLanguage())).toList();
    }
}
