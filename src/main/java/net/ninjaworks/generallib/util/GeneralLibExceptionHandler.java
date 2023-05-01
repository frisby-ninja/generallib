package net.ninjaworks.generallib.util;

/**
 * Used instead of a try/catch block for any custom exceptions thrown in the library.
 */
public interface GeneralLibExceptionHandler {

    /**
     * Handles an InvalidAnnotationsException.
     * @param e The exception
     */
    void handle(InvalidAnnotationsException e);

    /**
     * Handles an InvalidMethodParamsException.
     * @param e The exception
     */
    void handle(InvalidMethodParamsException e);

    /**
     * Handles an InvalidConstructorParamsException.
     * @param e The exception
     */
    void handle(InvalidConstructorParamsException e);
}