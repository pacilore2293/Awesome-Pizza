package it.lorenzopaciello.awesomepizza.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {

    @NotNull(message = "{NotNull.orderDto.name}")
    @NotBlank(message = "{NotBlank.orderDto.name}")
    private String name;

    @NotNull(message = "{NotNull.orderDto.lastName}")
    @NotBlank(message = "{NotBlank.orderDto.lastName}")
    private String lastName;

    @NotNull(message = "{NotNull.orderDto.email}")
    @NotBlank(message = "{NotBlank.orderDto.email}")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "{Pattern.orderDto.email}"
    )
    private String email;

    @NotNull(message = "{NotNull.orderDto.phone}")
    @NotBlank(message = "{NotBlank.orderDto.phone}")
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "{Pattern.orderDto.phone}"
    )
    private String phone;

    @NotEmpty(message = "{NotEmpty.orderDto.order}")
    @Size(min = 1, message = "Positive.orderDto.order.quantity")
    @Valid
    private List<PizzaOrderDto> order;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PizzaOrderDto {

        @NotNull(message = "NotNull.orderDto.order.idPizza")
        private Long idPizza;

        @NotNull(message = "NotNull.orderDto.order.quantity")
        private Integer quantity;
    }
}
