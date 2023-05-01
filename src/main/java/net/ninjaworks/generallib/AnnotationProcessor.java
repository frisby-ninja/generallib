package net.ninjaworks.generallib;

import net.ninjaworks.generallib.util.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Simple annotation processor.
 */
public class AnnotationProcessor {

    private Class<? extends Annotation> target;

    /**
     * Runs checks on all annotation classes that have the AutoHandle annotation.
     * <i>The AutoHandle annotation is pointless without running this method</i>.
     * Unlike its counterpart with the MissingAnnotationHandler parameter,
     * it will simply throw a RuntimeException when it runs into any internal Exception
     */
    @SuppressWarnings("unchecked")
    public static void init() {
        Set<Class<?>> annotations = ReflectionUtils.getAllClassesWithAnnotation(AutoHandle.class);
        AnnotationProcessor processor = null;
        for(Class<?> annotation : annotations) {
            if(annotation.isAnnotationPresent(RequiredAnnotations.class)) {
                Set<Class<?>> classes = ReflectionUtils.getAllClassesWithAnnotation((Class<? extends Annotation>) annotation);
                if (classes.size() > 0) {
                    if (processor == null) {
                        processor = new AnnotationProcessor((Class<? extends Annotation>) annotation);
                    } else {
                        processor.setTarget((Class<? extends Annotation>) annotation);
                    }
                    for (Class<?> clazz : classes) {
                        Class<? extends Annotation>[] missingAnnotations = processor.getMissingExpectedAnnotations(clazz);
                        if (missingAnnotations != null && missingAnnotations.length > 0) {
                            throw new RuntimeException(new InvalidAnnotationsException(missingAnnotations, clazz));
                        }
                    }
                }
            }
            if(annotation.isAnnotationPresent(RequiredMethodParams.class)) {
                Set<Method> methods = ReflectionUtils.getAllMethodsWithAnnotation((Class<? extends Annotation>) annotation);
                if (methods.size() > 0) {
                    if (processor == null) {
                        processor = new AnnotationProcessor((Class<? extends Annotation>) annotation);
                    } else {
                        processor.setTarget((Class<? extends Annotation>) annotation);
                    }
                    for (Method method : methods) {
                        Class<?>[] missingParams = processor.getMissingExpectedMethodParams(method);
                        if (missingParams != null && missingParams.length > 0) {
                            throw new RuntimeException(new InvalidMethodParamsException(missingParams, method));
                        }
                    }
                }
            }
            if(annotation.isAnnotationPresent(RequiredConstructorParams.class)) {
                Set<Class<?>> classes = ReflectionUtils.getAllClassesWithAnnotation((Class<? extends Annotation>) annotation);
                if (classes.size() > 0) {
                    if (processor == null) {
                        processor = new AnnotationProcessor((Class<? extends Annotation>) annotation);
                    } else {
                        processor.setTarget((Class<? extends Annotation>) annotation);
                    }
                    for (Class<?> clazz : classes) {
                        Constructor<?>[] constructors = clazz.getConstructors();
                        if(constructors.length > 0) {
                            for (Constructor<?> constructor : constructors) {
                                Class<?>[] missingParams = processor.getMissingExpectedConstructorParams(constructor);
                                if (missingParams != null && missingParams.length > 0) {
                                    throw new RuntimeException(new InvalidConstructorParamsException(missingParams, constructor));
                                }
                            }
                        } else {
                            RequiredConstructorParams params = annotation.getAnnotation(RequiredConstructorParams.class);
                            throw new RuntimeException(new InvalidConstructorParamsException(params.value(), AnnotationProcessor.class.getDeclaredConstructors()[0]));
                        }
                    }
                }
            }
        }
    }

    /**
     * Runs checks on all annotation classes that have the AutoHandle annotation.
     * <i>The AutoHandle annotation is pointless without running this method</i>.
     * @param generalLibExceptionHandler The handler to use in the case of an internal exception
     */
    @SuppressWarnings("unchecked")
    public static void init(GeneralLibExceptionHandler generalLibExceptionHandler) {
        Set<Class<?>> annotations = ReflectionUtils.getAllClassesWithAnnotation(AutoHandle.class);
        AnnotationProcessor processor = null;
        for(Class<?> annotation : annotations) {
            if(annotation.isAnnotationPresent(RequiredAnnotations.class)) {
                Set<Class<?>> classes = ReflectionUtils.getAllClassesWithAnnotation((Class<? extends Annotation>) annotation);
                if (classes.size() > 0) {
                    if (processor == null) {
                        processor = new AnnotationProcessor((Class<? extends Annotation>) annotation);
                    } else {
                        processor.setTarget((Class<? extends Annotation>) annotation);
                    }
                    for (Class<?> clazz : classes) {
                        Class<? extends Annotation>[] missingAnnotations = processor.getMissingExpectedAnnotations(clazz);
                        if (missingAnnotations != null && missingAnnotations.length > 0) {
                            generalLibExceptionHandler.handle(new InvalidAnnotationsException(missingAnnotations, clazz));
                        }
                    }
                }
            }
            if(annotation.isAnnotationPresent(RequiredMethodParams.class)) {
                Set<Method> methods = ReflectionUtils.getAllMethodsWithAnnotation((Class<? extends Annotation>) annotation);
                if (methods.size() > 0) {
                    if (processor == null) {
                        processor = new AnnotationProcessor((Class<? extends Annotation>) annotation);
                    } else {
                        processor.setTarget((Class<? extends Annotation>) annotation);
                    }
                    for (Method method : methods) {
                        Class<?>[] missingParams = processor.getMissingExpectedMethodParams(method);
                        if (missingParams != null && missingParams.length > 0) {
                            generalLibExceptionHandler.handle(new InvalidMethodParamsException(missingParams, method));
                        }
                    }
                }
            }
            if(annotation.isAnnotationPresent(RequiredConstructorParams.class)) {
                Set<Class<?>> classes = ReflectionUtils.getAllClassesWithAnnotation((Class<? extends Annotation>) annotation);
                if (classes.size() > 0) {
                    if (processor == null) {
                        processor = new AnnotationProcessor((Class<? extends Annotation>) annotation);
                    } else {
                        processor.setTarget((Class<? extends Annotation>) annotation);
                    }
                    for (Class<?> clazz : classes) {
                        Constructor<?>[] constructors = clazz.getConstructors();
                        if(constructors.length > 0) {
                            for (Constructor<?> constructor : constructors) {
                                Class<?>[] missingParams = processor.getMissingExpectedConstructorParams(constructor);
                                if (missingParams != null && missingParams.length > 0) {
                                    generalLibExceptionHandler.handle(new InvalidConstructorParamsException(missingParams, constructor));
                                }
                            }
                        } else {
                            RequiredConstructorParams params = annotation.getAnnotation(RequiredConstructorParams.class);
                            generalLibExceptionHandler.handle(new InvalidConstructorParamsException(params.value(), null));
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates a new instance of the processor.
     * @param target The annotation to use in processing
     */
    public AnnotationProcessor(Class<? extends Annotation> target) {
        this.target = target;
    }

    /**
     * Sets the processor's target to the given annotation.
     * @param target The annotation to target
     */
    public void setTarget(Class<? extends Annotation> target) {
        this.target = target;
    }

    /**
     * Checks whether the given class is annotated with the annotations required by the target annotation.
     * @param clazz The class to perform the checks on
     * @return The result of the check, or false if the target annotation is null
     */
    @SuppressWarnings("unchecked")
    public boolean doesClassHaveExpectedAnnotations(Class<?> clazz) {
        if(target != null) {
            RequiredAnnotations requiredAnnotations = target.getAnnotation(RequiredAnnotations.class);
            if(requiredAnnotations != null) {
                Class<? extends Annotation>[] annotationClasses = requiredAnnotations.value();
                Annotation[] annotations = clazz.getAnnotations();
                if(annotations.length >= annotationClasses.length + 1) {
                    Class<? extends Annotation>[] annotationClazzes = (Class<? extends Annotation>[]) new Class<?>[annotations.length];
                    ArrayManipulator<Class<? extends Annotation>> classArrayManipulator = new ArrayManipulator<>();
                    for (int i = 0; i < annotations.length; i++) {
                        Annotation annotation = annotations[i];
                        if (!annotation.annotationType().equals(target)) {
                            annotationClazzes[i] = annotation.annotationType();
                        }
                    }
                    classArrayManipulator.setArray(annotationClazzes);
                    for (Class<? extends Annotation> annotationClass : annotationClasses) {
                        if (!classArrayManipulator.arrayContainsElement(annotationClass)) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Gets all the missing annotations in the given class required by the target annotation
     * @param clazz The class to perform the operations on
     * @return The missing annotations, or null if the target annotation is null
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Annotation>[] getMissingExpectedAnnotations(Class<?> clazz) {
        if(target != null) {
            RequiredAnnotations requiredAnnotations = target.getAnnotation(RequiredAnnotations.class);
            if(requiredAnnotations != null) {
                Class<? extends Annotation>[] missingAnnotations = new Class[0];
                Class<? extends Annotation>[] annotationClasses = requiredAnnotations.value();
                Annotation[] annotations = clazz.getAnnotations();
                if(annotations.length >= annotationClasses.length + 1) {
                    Class<? extends Annotation>[] annotationClazzes = new Class[annotations.length];
                    ArrayManipulator<Class<? extends Annotation>> classArrayManipulator = new ArrayManipulator<>();
                    for (int i = 0; i < annotations.length; i++) {
                        Annotation annotation = annotations[i];
                        if (!annotation.annotationType().equals(target)) {
                            annotationClazzes[i] = annotation.annotationType();
                        }
                    }
                    classArrayManipulator.setArray(annotationClazzes);
                    for (Class<? extends Annotation> annotationClass : annotationClasses) {
                        if (!classArrayManipulator.arrayContainsElement(annotationClass)) {
                            classArrayManipulator.setArray(missingAnnotations);
                            missingAnnotations = classArrayManipulator.addElement(annotationClass);
                        }
                    }
                    return missingAnnotations;
                } else {
                    return requiredAnnotations.value();
                }
            }
        }
        return null;
    }

    /**
     * Checks whether the given method contains the parameters required by the target annotation
     * @param method The method to perform the checks on
     * @return The result of the check, or false if the target annotation is null
     */
    public boolean doesMethodContainExpectedParams(Method method) {
        if(target != null) {
            RequiredMethodParams requiredMethodParams = target.getAnnotation(RequiredMethodParams.class);
            if (requiredMethodParams != null) {
                Class<?>[] expectedParams = requiredMethodParams.value();
                RequirementType type = requiredMethodParams.requirementType();
                if (expectedParams != null && expectedParams.length > 0) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    switch (type) {
                        case FIRST: {
                            if (parameterTypes.length >= expectedParams.length) {
                                for (int i = 0; i < expectedParams.length; i++) {
                                    if (!expectedParams[i].equals(parameterTypes[i])) {
                                        return false;
                                    }
                                }
                            } else {
                                return false;
                            }
                        }
                        case ONLY: {
                            if (expectedParams.length != parameterTypes.length) {
                                return false;
                            }
                            for (int i = 0; i < expectedParams.length; i++) {
                                if (!expectedParams[i].equals(parameterTypes[i])) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Gets all the missing parameters in the given method required by the target annotation.
     * @param method The method to perform the operations on
     * @return The missing parameters, or null if the target annotation is null
     */
    public Class<?>[] getMissingExpectedMethodParams(Method method) {
        if(target != null) {
            RequiredMethodParams requiredMethodParams = target.getAnnotation(RequiredMethodParams.class);
            if (requiredMethodParams != null) {
                Class<?>[] missingParams = new Class[0];
                ArrayManipulator<Class<?>> classArrayManipulator = new ArrayManipulator<>();
                Class<?>[] expectedParams = requiredMethodParams.value();
                if (expectedParams != null && expectedParams.length > 0) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if(parameterTypes.length == 0) {
                        return expectedParams;
                    }
                    for (int i = 0; i < expectedParams.length; i++) {
                        if (!expectedParams[i].equals(parameterTypes[i])) {
                            classArrayManipulator.setArray(missingParams);
                            missingParams = classArrayManipulator.addElement(expectedParams[i]);
                        }
                    }
                    if(requiredMethodParams.requirementType().equals(RequirementType.ONLY) && parameterTypes.length != expectedParams.length) {
                        missingParams = new Class[]{null};
                    }
                }
                return missingParams;
            }
        }
        return null;
    }

    /**
     * Checks whether the given constructor contains the parameters required by the target annotation
     * @param constructor The constructor to perform the checks on
     * @return The result of the check, or false if the target annotation is null
     */
    public boolean doesConstructorContainExpectedParams(Constructor<?> constructor) {
        if(target != null) {
            RequiredConstructorParams requiredConstructorParams = target.getAnnotation(RequiredConstructorParams.class);
            if (requiredConstructorParams != null) {
                Class<?>[] expectedParams = requiredConstructorParams.value();
                RequirementType type = requiredConstructorParams.requirementType();
                if (expectedParams != null && expectedParams.length > 0) {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    switch (type) {
                        case FIRST: {
                            if (parameterTypes.length >= expectedParams.length) {
                                for (int i = 0; i < expectedParams.length; i++) {
                                    if (!expectedParams[i].equals(parameterTypes[i])) {
                                        return false;
                                    }
                                }
                            } else {
                                return false;
                            }
                        }
                        case ONLY: {
                            if (expectedParams.length != parameterTypes.length) {
                                return false;
                            }
                            for (int i = 0; i < expectedParams.length; i++) {
                                if (!expectedParams[i].equals(parameterTypes[i])) {
                                    return false;
                                }
                            }
                        }
                    }
                    ;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Gets all the missing parameters in the given constructor required by the target annotation.
     * @param constructor The constructor to perform the operations on
     * @return The missing parameters, or null if the target annotation is null
     */
    public Class<?>[] getMissingExpectedConstructorParams(Constructor<?> constructor) {
        if(target != null) {
            RequiredConstructorParams requiredConstructorParams = target.getAnnotation(RequiredConstructorParams.class);
            if (requiredConstructorParams != null) {
                Class<?>[] missingParams = new Class[0];
                ArrayManipulator<Class<?>> classArrayManipulator = new ArrayManipulator<>();
                Class<?>[] expectedParams = requiredConstructorParams.value();
                if (expectedParams != null && expectedParams.length > 0) {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    if(parameterTypes.length == 0) {
                        return expectedParams;
                    }
                    for (int i = 0; i < expectedParams.length; i++) {
                        if (!expectedParams[i].equals(parameterTypes[i])) {
                            classArrayManipulator.setArray(missingParams);
                            missingParams = classArrayManipulator.addElement(expectedParams[i]);
                        }
                    }
                    if(requiredConstructorParams.requirementType().equals(RequirementType.ONLY) && parameterTypes.length != expectedParams.length) {
                        missingParams = new Class[]{null};
                    }
                }
                return missingParams;
            }
        }
        return null;
    }
}