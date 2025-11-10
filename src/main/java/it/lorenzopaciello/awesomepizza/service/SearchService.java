package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import it.lorenzopaciello.awesomepizza.exception.custom.NotFoundException;
import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.model.Role;
import it.lorenzopaciello.awesomepizza.model.User;
import it.lorenzopaciello.awesomepizza.repository.PizzaRepository;
import it.lorenzopaciello.awesomepizza.repository.RoleRepository;
import it.lorenzopaciello.awesomepizza.service.interfaces.SearchServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService implements SearchServiceInterface {

    private PizzaRepository pizzaRepository;
    private RoleRepository roleRepository;

    @Autowired
    public SearchService(PizzaRepository pizzaRepository, RoleRepository roleRepository){
        this.pizzaRepository = pizzaRepository;
        this.roleRepository = roleRepository;
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
    public User findUserByUsername(String username) {
        return null;
    }
}
