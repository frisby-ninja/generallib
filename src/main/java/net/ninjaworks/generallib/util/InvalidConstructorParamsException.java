package net.ninjaworks.generallib.util;

import java.lang.reflect.Constructor;

/**
 * Thrown in the library when the annotation processor doesn't find a valid parameter configuration
 * in the given constructor required by the processed annotation.
 */
public class InvalidConstructorParamsException extends Exception {

    public final Class<?> params[];
    public final Constructor<?> thrower;

    /**
     * Creates a new InvalidConstructorParamsException.
     * @param params The invalid parameter(s)
     * @param thrower The constructor that has caused the creation of the exception
     */
    public InvalidConstructorParamsException(Class<?>[] params, Constructor<?> thrower) {
        super("Invalid parameter configuration in constructor of " + thrower.getName() + "!");
        this.params = params;
        this.thrower = thrower;
    }
}