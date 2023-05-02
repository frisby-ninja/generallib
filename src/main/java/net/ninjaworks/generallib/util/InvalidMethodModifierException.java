package net.ninjaworks.generallib.util;

import java.lang.reflect.Method;

/**
 * Thrown in the library when a method's modifier (static or not) does not meet the required arguments.
 */
public class InvalidMethodModifierException extends Exception {

    public final Method thrower;

    /**
     * Creates a new InvalidMethodModifierException.
     * @param thrower The method that has caused the creation of the exception
     */
    public InvalidMethodModifierException(Method thrower) {
        super("Invalid method modifier at " + thrower.getName() + "!");
        this.thrower = thrower;
    }
}