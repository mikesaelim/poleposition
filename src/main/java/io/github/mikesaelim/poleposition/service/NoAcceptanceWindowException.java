package io.github.mikesaelim.poleposition.service;

/**
 * Exception thrown when no valid acceptance window was found.
 */
public class NoAcceptanceWindowException extends Exception {

    public NoAcceptanceWindowException() {
        super();
    }

    public NoAcceptanceWindowException(String message) {
        super(message);
    }

    public NoAcceptanceWindowException(Throwable cause) {
        super(cause);
    }

    public NoAcceptanceWindowException(String message, Throwable cause) {
        super(message, cause);
    }

}
