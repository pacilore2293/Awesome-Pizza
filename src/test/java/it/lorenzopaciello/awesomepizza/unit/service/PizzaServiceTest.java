package it.lorenzopaciello.awesomepizza.unit.service;

import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.controller.dto.response.PizzaResponseDto;
import it.lorenzopaciello.awesomepizza.repository.PizzaRepository;
import it.lorenzopaciello.awesomepizza.service.PizzaService;
import it.lorenzopaciello.awesomepizza.unit.utils.PizzaUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PizzaServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @InjectMocks
    private PizzaService pizzaService;

    @Nested
    @DisplayName("Tests per il metodo getAllPizzas successo")
    class GetAllPizzasTests {

        @Test
        @DisplayName("Dovrebbe restituire lista di pizze disponibili")
        void shouldReturnPizzaList_whenPizzasAvailable() {

            List<Pizza> pizzas = PizzaUtils.getPizzaList();

            when(pizzaRepository.findAllByAvailableIsTrue()).thenReturn(pizzas);

            List<PizzaResponseDto> allPizzas = pizzaService.getAllPizzas();

            assertEquals(3, allPizzas.size());

            verify(pizzaRepository, times(1)).findAllByAvailableIsTrue();
        }

        @Test
        @DisplayName("Dovrebbe restituire lista vuota")
        void shouldReturnPizzaList_whenPizzasEmpty() {

            List<Pizza> pizzas = PizzaUtils.getPizzaList();

            when(pizzaRepository.findAllByAvailableIsTrue()).thenReturn(List.of());

            List<PizzaResponseDto> allPizzas = pizzaService.getAllPizzas();

            assertEquals(0, allPizzas.size());

            verify(pizzaRepository, times(1)).findAllByAvailableIsTrue();
        }

    }

    @Nested
    @DisplayName("Test per il metodo getAllPizzas errore")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Dovrebbe lanciare NullPointerException se repository non è inject")
        void shouldThrowNpe_WhenRepositoryIsNull() {

            PizzaService brokenService = new PizzaService(null);

            assertThrows(NullPointerException.class, brokenService::getAllPizzas);
        }
    }

}
