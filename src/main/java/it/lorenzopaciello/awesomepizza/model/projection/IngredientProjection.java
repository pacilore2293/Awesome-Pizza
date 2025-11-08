package it.lorenzopaciello.awesomepizza.model.projection;

import it.lorenzopaciello.awesomepizza.model.Ingredient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientProjection {

    private String name;

    public IngredientProjection(Ingredient ingredient, String language){
        this.name = language.equalsIgnoreCase("en") ? ingredient.getNameEng() : ingredient.getNameIta();
    }
}
