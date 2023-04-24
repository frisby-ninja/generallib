package net.ninjaworks.generallib;

import org.reflections.Reflections;

public class ClassUtils {

    /**
     * Gets all the classes that implement the given interface.
     * @param interface_ The interface filter
     * @return The discovered classes
     */
    @SuppressWarnings("unchecked")
    public static <T> Object[] getAllImplementationsOf(T interface_) {
        Reflections reflections = new Reflections(interface_.getClass().getPackage());
        //Set<Class<? extends Pet>> classes = reflections.getSubTypesOf(Pet.class);
        return null;
    }
}