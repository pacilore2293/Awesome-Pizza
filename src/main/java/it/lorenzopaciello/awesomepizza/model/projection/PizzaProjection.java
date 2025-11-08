package it.lorenzopaciello.awesomepizza.model.projection;

import it.lorenzopaciello.awesomepizza.model.Pizza;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PizzaProjection {

    private Long id;
    private String name;
    private String description;
    private Double price;
    List<IngredientProjection> ingredients;

    public PizzaProjection(Pizza pizza, String language){
        this.id = pizza.getId();
        this.name = language.equalsIgnoreCase("en") ? pizza.getNameEng() : pizza.getNameIta();
        this.description = language.equalsIgnoreCase("en") ? pizza.getDescriptionEng() : pizza.getDescriptionIta();
        this.price = pizza.getPrice();

        if(pizza.getPizzaIngredients() != null && !pizza.getPizzaIngredients().isEmpty()){
            this.ingredients = pizza.getPizzaIngredients().stream().map(pizzaIngredient -> new IngredientProjection(pizzaIngredient.getIngredient(), language)).toList();
        }
    }
}
