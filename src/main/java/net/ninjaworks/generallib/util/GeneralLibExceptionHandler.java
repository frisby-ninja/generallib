package net.ninjaworks.generallib.util;

/**
 * Used instead of a try/catch block for any custom exceptions thrown in the library.
 */
public interface GeneralLibExceptionHandler {

    /**
     * Handles an {@link InvalidAnnotationsException}.
     * @param e The exception
     */
    void handle(InvalidAnnotationsException e);

    /**
     * Handles an {@link InvalidMethodParamsException}.
     * @param e The exception
     */
    void handle(InvalidMethodParamsException e);

    /**
     * Handles an {@link InvalidConstructorParamsException}.
     * @param e The exception
     */
    void handle(InvalidConstructorParamsException e);

    /**
     * Handles an {@link InvalidMethodModifierException}
     * @param e The exception
     */
    void handle(InvalidMethodModifierException e);
}