package it.lorenzopaciello.awesomepizza.repository;

import it.lorenzopaciello.awesomepizza.model.Ingredient;
import it.lorenzopaciello.awesomepizza.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    Optional<Pizza> findByIdAndAvailableIsTrue(Long id);
    List<Pizza> findAllByAvailableIsTrue();
}
