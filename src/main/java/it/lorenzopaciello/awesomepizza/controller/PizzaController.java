package it.lorenzopaciello.awesomepizza.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.lorenzopaciello.awesomepizza.controller.dto.response.PizzaResponseDto;
import it.lorenzopaciello.awesomepizza.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Pizza", description = "Gestione delle pizze")
@RestController("needController")
@RequestMapping("/api")
public class PizzaController {

    private PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService){
        this.pizzaService = pizzaService;
    }

    @Operation(summary = "Ottiene la lista delle pizze disponibili", description = "Restituisce l'elenco completo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista ottenuta con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                 [
                                  {
                                      "id": 1,
                                      "name": "Margherita",
                                      "description": "Classic pizza with tomato, mozzarella, and fresh basil.",
                                      "price": 6.0,
                                      "ingredients": [
                                          {
                                              "name": "Tomato"
                                          },
                                          {
                                              "name": "Tomato"
                                          },
                                          {
                                              "name": "Tomato"
                                          },
                                          {
                                              "name": "Basil"
                                          },
                                          {
                                              "name": "Tomato"
                                          },
                                          {
                                              "name": "Tomato"
                                          },
                                          {
                                              "name": "Basil"
                                          },
                                          {
                                              "name": "Basil"
                                          },
                                          {
                                              "name": "Basil"
                                          },
                                          {
                                              "name": "Mozzarella cheese"
                                          },
                                          {
                                              "name": "Basil"
                                          },
                                          {
                                              "name": "Mozzarella cheese"
                                          },
                                          {
                                              "name": "Mozzarella cheese"
                                          },
                                          {
                                              "name": "Basil"
                                          },
                                          {
                                              "name": "Mozzarella cheese"
                                          },
                                          {
                                              "name": "Tomato"
                                          },
                                          {
                                              "name": "Mozzarella cheese"
                                          },
                                          {
                                              "name": "Mozzarella cheese"
                                          }
                                      ]
                                  }
                                ]
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Errore interno del server",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                     "code": "GEN_001",
                                     "message": "Errore interno del server, contattare l'assistenza"
                                }
                            """
                            )
                    )
            )
    })
    @GetMapping(value = "/pizzas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PizzaResponseDto>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }
}
