package br.com.alura.ProjetoAlura.exceptions;

import br.com.alura.ProjetoAlura.util.ErrorItemDTO;

public class ErrorItemException extends RuntimeException {

    private ErrorItemDTO errorItem;

    public ErrorItemException(ErrorItemDTO errorItem) {
        super(errorItem.getMessage());
        this.errorItem = errorItem;
    }

    public ErrorItemDTO getErrorItem() {
        return errorItem;
    }
}
