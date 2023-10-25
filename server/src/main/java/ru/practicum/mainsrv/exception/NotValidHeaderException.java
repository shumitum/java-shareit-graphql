package ru.practicum.mainsrv.exception;

public class NotValidHeaderException extends RuntimeException {
    public NotValidHeaderException(String message) {
        super(message);
    }
}

