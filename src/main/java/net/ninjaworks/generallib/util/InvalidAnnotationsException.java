package net.ninjaworks.generallib.util;

import java.lang.annotation.Annotation;

/**
 * Thrown in the library when the annotation processor doesn't find a valid annotation configuration
 * in the given class required by the processed annotation.
 */
public class InvalidAnnotationsException extends Exception {

    public final Class<? extends Annotation> annotations[];
    public final Class<?> thrower;

    /**
     * Creates a new InvalidAnnotationsException.
     * @param annotations The invalid annotation(s)
     * @param thrower The class that has caused the creation of the exception
     */
    public InvalidAnnotationsException(Class<? extends Annotation>[] annotations, Class<?> thrower) {
        super("Invalid annotation configuration at " + thrower.getName() + "!");
        this.annotations = annotations;
        this.thrower = thrower;
    }
}