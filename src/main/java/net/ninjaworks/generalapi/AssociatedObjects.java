package net.ninjaworks.generalapi;

/**
 * Like a hashmap, but limited to one element.
 * @param obj1 The first object to store
 * @param obj2 The second object to store
 */
public record AssociatedObjects(Object obj1, Object obj2) {

    private static AssociatedObjects[] instances = new AssociatedObjects[0];
    private static ArrayManipulator<AssociatedObjects> arrayManipulator = new ArrayManipulator<>();

    public AssociatedObjects(Object obj1, Object obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        arrayManipulator.setArray(instances);
        instances = arrayManipulator.addElement(this);
    }

    /**
     * Gets an associated object instance from the given objects.
     * @param obj1 The first object of the wanted instance, if null the instance will be found using
     *             only the second object
     * @param obj2 The second object of the wanted instance, if null the instance will be found using
     *             only the first object
     * @return The found instance, null if it fails
     */
    public static AssociatedObjects getInstance(Object obj1, Object obj2) {
        AssociatedObjects obj = null;
        if(obj1 == null && obj2 == null) {
            return null;
        }
        if(obj2 == null) {
            return getInstanceFromFirstObj(obj1);
        }
        if(obj1 == null) {
            return getInstanceFromSecondObj(obj2);
        }
        for(AssociatedObjects object : instances) {
            if(object.obj1.equals(obj1) && object.obj2.equals(obj2)) {
                obj = object;
            }
        }
        return obj;
    }

    private static AssociatedObjects getInstanceFromFirstObj(Object obj1) {
        AssociatedObjects obj = null;
        for(AssociatedObjects object : instances) {
            if(object.obj1.equals(obj1)) {
                obj = object;
            }
        }
        return obj;
    }

    private static AssociatedObjects getInstanceFromSecondObj(Object obj2) {
        AssociatedObjects obj = null;
        for(AssociatedObjects object : instances) {
            if(object.obj2.equals(obj2)) {
                obj = object;
            }
        }
        return obj;
    }
}