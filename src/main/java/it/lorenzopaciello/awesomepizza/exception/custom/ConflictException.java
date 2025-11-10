package it.lorenzopaciello.awesomepizza.exception.custom;

import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException{

    private final ErrorCode errorCode;

    public ConflictException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }
}

