package it.lorenzopaciello.awesomepizza.repository;

import it.lorenzopaciello.awesomepizza.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
