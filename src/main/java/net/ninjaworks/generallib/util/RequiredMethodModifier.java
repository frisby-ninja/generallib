package net.ninjaworks.generallib.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When annotated on another annotation,
 * any method that is annotated with said annotation will be required to either be static or not,
 * depending on the given value.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredMethodModifier {
    /**
     * Whether the method should be required to be static
     * @return The above
     */
    boolean value();
}