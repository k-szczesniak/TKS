package com.mycompany.firstapplication.Exceptions;

public class RepositoryException extends IllegalArgumentException {
    public RepositoryException(String s) {
        super(s);
    }
}
