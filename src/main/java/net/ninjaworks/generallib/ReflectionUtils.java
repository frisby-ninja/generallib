package net.ninjaworks.generallib;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtils {

    /**
     * Gets all the classes that implement the given interface.
     * @param interface_ The interface filter
     * @return The discovered classes
     */
    @SuppressWarnings("unchecked")
    public static Set<Class<?>> getAllImplementationsOf(Class<?> interface_) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath()));
        return (Set<Class<?>>) reflections.getSubTypesOf(interface_);
    }

    /**
     * Gets all the compiled classes that have the given annotation
     * @param annotation The annotation
     * @return The found classes
     */
    public static Set<Class<?>> getAllClassesWithAnnotation(Class<? extends Annotation> annotation) {
        if(annotation != null) {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forJavaClassPath()));
            return reflections.getTypesAnnotatedWith(annotation);
        }
        return new HashSet<>();
    }

    /**
     * Gets all the compiled methods that have the given annotation
     * @param annotation The annotation
     * @return The found methods
     */
    public static Set<Method> getAllMethodsWithAnnotation(Class<? extends Annotation> annotation) {
        if(annotation != null) {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forJavaClassPath()).setScanners(Scanners.MethodsAnnotated));
            return reflections.getMethodsAnnotatedWith(annotation);
        }
        return new HashSet<>();
    }
}