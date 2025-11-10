package it.lorenzopaciello.awesomepizza.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseErrorDto {
    private String code;
    private String message;
}
