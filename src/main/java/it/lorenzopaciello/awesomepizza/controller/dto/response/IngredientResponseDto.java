package it.lorenzopaciello.awesomepizza.controller.dto.response;

import it.lorenzopaciello.awesomepizza.model.Ingredient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientResponseDto {

    private String name;

    public IngredientResponseDto(Ingredient ingredient, String language){
        this.name = language.equalsIgnoreCase("en") ? ingredient.getNameEng() : ingredient.getNameIta();
    }
}
