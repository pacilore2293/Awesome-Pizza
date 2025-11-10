package it.lorenzopaciello.awesomepizza.exception.custom;

import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{

    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

}

