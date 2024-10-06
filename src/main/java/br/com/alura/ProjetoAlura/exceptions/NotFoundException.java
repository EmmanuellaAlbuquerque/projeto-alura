package br.com.alura.ProjetoAlura.exceptions;

import br.com.alura.ProjetoAlura.util.ErrorItemDTO;

public class NotFoundException extends ErrorItemException {
    public NotFoundException(ErrorItemDTO errorItem) {
        super(errorItem);
    }
}
