package it.lorenzopaciello.awesomepizza.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.lorenzopaciello.awesomepizza.controller.dto.response.OrderResponseDto;
import it.lorenzopaciello.awesomepizza.controller.dto.request.OrderRequestDto;
import it.lorenzopaciello.awesomepizza.model.enums.OrderActionEnum;
import it.lorenzopaciello.awesomepizza.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestione ordini", description = "Gestione completa degli ordino")
@RestController("orderController")
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @Operation(
            summary = "Creazione ordine",
            description = "Restituisce l'ordine creato, con codice",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Creazione ordine esempio",
                                    summary = "",
                                    value = """
                                    {
                                         "name": "Mario",
                                         "lastName": "Rossi",
                                         "email": "pprova@gmai.com",
                                         "phone": "3333333333",
                                         "order": [
                                             {"idPizza": 1, "quantity": 2},
                                             {"idPizza": 1, "quantity": 2},
                                             {"idPizza": 6, "quantity": 10}
                                         ]
                                     }
                                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Ordine creato con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNjg1NTQ4MDAwLCJleHAiOjE2ODU1NTAwMDB9.Yy9W-JmUPZ8wb3hDoK9xsw1oqlTnTxJHYeIDGlMdoT0"
                                }
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.createOrder(orderRequestDto));
    }


    @Operation(summary = "Dettaglio ordine", description = "Restituisce il dettaglio ordine")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dettaglio ottenuto con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                "id": 2,
                                "nameGuest": "Mario",
                                "lastNameGuest": "Rossi",
                                "orderCode": "ORD-20251111-87FA3FFF",
                                "totalPrice": 99,
                                "status": "IN_ATTESA",
                                "details": [
                                  {
                                    "namePizza": "Margherita",
                                    "price": 6,
                                    "quantity": 4
                                  },
                                  {
                                    "namePizza": "Vegetariana",
                                    "price": 7.5,
                                    "quantity": 10
                                  }
                                ]
                              }
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
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> getDetailOrder(@RequestParam(value = "orderCode") String orderCode) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.orderDetails(orderCode));
    }

    @Operation(summary = "Lista ordini", description = "Ricerca ordini per statpo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista ottenuta con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [{
                                   "id": 3,
                                   "nameGuest": "Mario",
                                   "lastNameGuest": "Rossi",
                                   "orderCode": "ORD-20251111-4642B39D",
                                   "totalPrice": 99,
                                   "status": "IN_ATTESA",
                                   "details": [
                                     {
                                       "namePizza": "Vegetariana",
                                       "price": 7.5,
                                       "quantity": 10
                                     },
                                     {
                                       "namePizza": "Margherita",
                                       "price": 6,
                                       "quantity": 4
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
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResponseDto>> searchOrders(@RequestParam(value = "status", required = false) OrderActionEnum searchStatus) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.searchStatus(searchStatus));
    }

    @Operation(summary = "Presa in carico ordine", description = "Prende in carico un'ordine")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ordine preso in carico con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 2,
                                    "nameGuest": "Mario",
                                    "lastNameGuest": "Rossi",
                                    "orderCode": "ORD-20251111-87FA3FFF",
                                    "totalPrice": 99.0,
                                    "status": "PRESO_IN_CARICO",
                                    "details": [
                                        {
                                            "namePizza": "Vegetariana",
                                            "price": 7.5,
                                            "quantity": 10
                                        },
                                        {
                                            "namePizza": "Margherita",
                                            "price": 6.0,
                                            "quantity": 4
                                        }
                                    ]
                                }
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
    @PutMapping(value = "/taken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> takenOrder(@RequestParam(name = "idOrder") Long idOrder, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.taken(idOrder, request));
    }

    @Operation(summary = "Completa presa in carico ordine", description = "Completa presa in carico ordine")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ordine preso in carico completato",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 2,
                                    "nameGuest": "Mario",
                                    "lastNameGuest": "Rossi",
                                    "orderCode": "ORD-20251111-87FA3FFF",
                                    "totalPrice": 99.0,
                                    "status": "PRONTO_PER_LA_CONSEGNA",
                                    "details": [
                                        {
                                            "namePizza": "Vegetariana",
                                            "price": 7.5,
                                            "quantity": 10
                                        },
                                        {
                                            "namePizza": "Margherita",
                                            "price": 6.0,
                                            "quantity": 4
                                        }
                                    ]
                                }
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
    @PutMapping(value = "/ready", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> readyOrder(@RequestParam(name = "idOrder") Long idOrder, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.ready(idOrder, request));
    }

    @Operation(summary = "Evasione ordine", description = "Evasione ordine")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ordine evaso con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 2,
                                    "nameGuest": "Mario",
                                    "lastNameGuest": "Rossi",
                                    "orderCode": "ORD-20251111-87FA3FFF",
                                    "totalPrice": 99.0,
                                    "status": "EVASO",
                                    "details": [
                                        {
                                            "namePizza": "Vegetariana",
                                            "price": 7.5,
                                            "quantity": 10
                                        },
                                        {
                                            "namePizza": "Margherita",
                                            "price": 6.0,
                                            "quantity": 4
                                        }
                                    ]
                                }
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
    @PutMapping(value = "/escape", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> escapeOrder(@RequestParam(name = "idOrder") Long idOrder, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.escape(idOrder, request));
    }

}
