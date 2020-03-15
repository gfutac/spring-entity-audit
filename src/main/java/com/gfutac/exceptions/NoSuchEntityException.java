package com.gfutac.exceptions;

public class NoSuchEntityException extends RuntimeException {

    private final String id;

    public NoSuchEntityException(String id, String message) {
        super(message);
        this.id = id;
    }

    public NoSuchEntityException(Long id, String message) {
        super(message);
        this.id = String.valueOf(id);
    }

    public NoSuchEntityException(Integer id, String message) {
        super(message);
        this.id = String.valueOf(id);
    }

    public String getId() {
        return id;
    }
}
