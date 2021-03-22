package com.isagron.accountmi_server.exceptions;

public class DocumentNotFountException extends RuntimeException {

    public DocumentNotFountException(String id, String type) {
        super("Failed to find " + type + " with id: " + id);
    }
}
