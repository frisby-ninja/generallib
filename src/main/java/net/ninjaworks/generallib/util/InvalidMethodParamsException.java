package net.ninjaworks.generallib.util;

import java.lang.reflect.Method;

/**
 * Thrown in the library when the annotation processor doesn't find a valid parameter configuration
 * in the given method required by the processed annotation.
 */
public class InvalidMethodParamsException extends Exception {

    public final Class<?> params[];
    public final Method thrower;

    /**
     * Creates a new InvalidMethodParamsException.
     * @param params The invalid parameter(s)
     * @param thrower The method that has caused the creation of the exception
     */
    public InvalidMethodParamsException(Class<?>[] params, Method thrower) {
        super("Invalid parameter configuration in method " + thrower.getDeclaringClass().getName() + "." + thrower.getName() + "!");
        this.params = params;
        this.thrower = thrower;
    }
}