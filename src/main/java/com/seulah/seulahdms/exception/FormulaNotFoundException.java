package com.seulah.seulahdms.exception;

public class FormulaNotFoundException extends RuntimeException {

    public FormulaNotFoundException(Long id) {
        super("Formula not found with id: " + id);
    }
}
