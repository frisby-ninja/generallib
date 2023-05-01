package net.ninjaworks.generallib.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that when applied to another annotation,
 * requires the given parameters of any constructor present in any class annotated with the latter.
 * The check is not explicitly performed;
 *  You will need to use an AnnotationProcessor (connected to the targeted annotation) to perform the check;
 *  or to add the AutoHandle annotation to the implementing annotation.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredConstructorParams {
    /**
     * The class list version of the required parameters.
     * @return The list
     */
    Class<?>[] value();

    /**
     * The requirement type: whether the required parameters should come first or be the only parameters.
     * @return The requirement type
     */
    RequirementType requirementType() default RequirementType.FIRST;
}