package net.ninjaworks.generallib;

import net.ninjaworks.generallib.util.RequiredMethodParams;

import javax.naming.OperationNotSupportedException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Simple annotation processor.
 */
public class AnnotationProcessor {

    private final Class<? extends Annotation> target;

    /**
     * Creates a new instance of the processor.
     * @param target The annotation to use in processing
     */
    public AnnotationProcessor(Class<? extends Annotation> target) {
        this.target = target;
    }

    public boolean doesMethodContainExpectedParams(Method method) throws OperationNotSupportedException {
        RequiredMethodParams requiredMethodParams = target.getAnnotation(RequiredMethodParams.class);
        if (requiredMethodParams != null) {
            Class<?>[] expectedParams = requiredMethodParams.value();
            if (expectedParams != null && expectedParams.length > 0) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length >= expectedParams.length) {
                    for (int i = 0; i < expectedParams.length; i++) {
                        if (!expectedParams[i].equals(parameterTypes[i])) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        } else {
            throw new OperationNotSupportedException("Can not test for the required params if target annotation class does not contain the " +
                    "'RequiredMethodParamsAnnotation'! (target = " + target.getName() + ")");
        }
        return true;
    }

    public static class Registry {

        private static Object[] registered = new Object[0];
        private static final ArrayManipulator<Object> objectArrayManipulator = new ArrayManipulator<>();

        public static void add(Class<? extends Annotation> annotation) {
            objectArrayManipulator.setArray(registered);
            registered = objectArrayManipulator.addElement(annotation);
        }
    }
}