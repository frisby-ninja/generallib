package net.ninjaworks.generallib.util;

import java.lang.annotation.*;

/**
 * An annotation that when applied to another annotation,
 * requires the given annotations of any class annotated with the latter.
 * The check is not explicitly performed;
 *  You will need to use an AnnotationProcessor (connected to the targeted annotation) to perform the check
 *  (or to add the AutoHandle annotation to the implementing annotation).
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredAnnotations {
    /**
     * The list of the required annotations.
     * @return The list.
     */
    Class<? extends Annotation>[] value();
}