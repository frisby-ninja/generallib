package net.ninjaworks.generalapi;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class MethodInfo {

    private final String name;
    private final int availability;
    private final boolean isStatic;
    private final Function[] functions;
    private final Object returnType;
    private final AssociatedObjects[] params;

    /**
     * DO NOT CALL UNLESS YOU KNOW WHAT YOU'RE DOING!
     */
    protected MethodInfo() {
        this.name = null;
        this.availability = -1;
        this.isStatic = false;
        this.functions = null;
        this.returnType = null;
        this.params = null;
    }

    public MethodInfo(String name, int availability, boolean isStatic, Function[] functions, Class<?> returnType, AssociatedObjects... params) {
        this.name = name;
        this.availability = availability;
        this.functions = functions;
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.params = Objects.requireNonNullElseGet(params, () -> new AssociatedObjects[0]);
    }

    /**
     * Gets all the necessary lines to write the method.
     * @return Two associated objects : The first is all the imports required,
     * whilst the second is the method itself
     */
    public AssociatedObjects toLines() {
        String[] imports = new String[0];
        String[] lines = new String[0];
        ArrayManipulator<String> stringArrayManipulator = new ArrayManipulator<>();
        StringBuilder builder = new StringBuilder();

        builder.append(Modifier.toString(availability));
        builder.append(" ");
        if(isStatic) {
            builder.append("static ");
        }
        String[] removedPackagesAndImport;
        if(returnType != null) {
            String classStr = returnType.toString();
            String[] splitClassStr = classStr.split(" ");
            StringBuilder className = new StringBuilder();
            for (int i = 1; i < splitClassStr.length; i++) {
                className.append(splitClassStr[i]);
            }
            removedPackagesAndImport = FileBasedClass.removePackageAndGetImport(className.toString());
            builder.append(removedPackagesAndImport[0]);

            for (int i = 1; i < removedPackagesAndImport.length; i++) {
                stringArrayManipulator.setArray(imports);
                imports = stringArrayManipulator.addElement(removedPackagesAndImport[i]);
            }
        } else {
            builder.append("void");
        }

        builder.append(" ");
        builder.append(name);
        builder.append("(");
        if(params.length > 0) {
            removedPackagesAndImport = FileBasedClass.removePackageAndGetImport(((Class<?>) params[0].obj1()).getName());
            builder.append(removedPackagesAndImport[0]).append(" ").append(params[0].obj2());
            stringArrayManipulator.setArray(imports);
            imports = stringArrayManipulator.addElement(removedPackagesAndImport[1]);
            for(int i = 1; i < params.length; i++) {
                builder.append(", ");
                removedPackagesAndImport = FileBasedClass.removePackageAndGetImport(((Class<?>) params[i].obj1()).getName());
                builder.append(removedPackagesAndImport[0]).append(" ").append(params[i].obj2());
                stringArrayManipulator.setArray(imports);
                imports = stringArrayManipulator.addElement(removedPackagesAndImport[1]);
            }
        }
        builder.append(")");
        builder.append(" {");

        stringArrayManipulator.setArray(lines);
        lines = stringArrayManipulator.addElement(builder.toString());
        if(functions != null) {
            for (Function function : functions) {
                stringArrayManipulator.setArray(imports);
                imports = stringArrayManipulator.joinToArray(function.imports, false);
                for(String line : function.lines) {
                    stringArrayManipulator.setArray(lines);
                    lines = stringArrayManipulator.addElement("    " + line);
                }
            }
        }
        stringArrayManipulator.setArray(lines);
        lines = stringArrayManipulator.addElement("}");

        return new AssociatedObjects(imports, lines);
    }

    public static int getModifierFromMethod(Method method) {
        int modifiers = method.getModifiers();
        if(Modifier.isStatic(modifiers)) {
            return Modifier.STATIC;
        }
        if(Modifier.isAbstract(modifiers)) {
            return Modifier.ABSTRACT;
        }
        if(Modifier.isSynchronized(modifiers)) {
            return Modifier.SYNCHRONIZED;
        }
        if(Modifier.isNative(modifiers)) {
            return Modifier.NATIVE;
        }
        if(Modifier.isStrict(modifiers)) {
            return Modifier.STRICT;
        }
        if(Modifier.isFinal(modifiers)) {
            return Modifier.FINAL;
        }
        return -1;
    }

    public static boolean isMethodStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    public static int getAvailabilityFromMethod(Method method) {
        int modifiers = method.getModifiers();
        if(Modifier.isPublic(modifiers)) {
            return Modifier.PUBLIC;
        }
        if(Modifier.isPrivate(modifiers)) {
            return Modifier.PRIVATE;
        }
        if(Modifier.isProtected(modifiers)) {
            return Modifier.PROTECTED;
        }
        return -1;
    }

    public String getName() {
        return name;
    }

    public AssociatedObjects[] getParams() {
        return params;
    }
}