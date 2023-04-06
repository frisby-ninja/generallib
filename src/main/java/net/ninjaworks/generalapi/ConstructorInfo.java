package net.ninjaworks.generalapi;

import java.lang.reflect.Modifier;
import java.util.Objects;

public class ConstructorInfo extends MethodInfo {

    private final String className;
    private final int availability;
    private final Function[] functions;
    private final AssociatedObjects[] params;

    public ConstructorInfo(String className, int availability, Function[] functions, AssociatedObjects... params) {
        super();
        this.className = className;
        this.availability = availability;
        this.functions = functions;
        this.params = Objects.requireNonNullElseGet(params, () -> new AssociatedObjects[0]);
    }

    @Override
    public AssociatedObjects toLines() {
        String[] imports = new String[0];
        String[] lines = new String[0];
        ArrayManipulator<String> stringArrayManipulator = new ArrayManipulator<>();
        StringBuilder builder = new StringBuilder();

        builder.append(Modifier.toString(availability));
        builder.append(" ");

        builder.append(className);

        builder.append("(");
        if(params.length > 0) {
            String[] removedPackagesAndImport = FileBasedClass.removePackageAndGetImport(((Class<?>) params[0].obj1()).getName());
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

    @Override
    public AssociatedObjects[] getParams() {
        return params;
    }

    @Override
    public String getName() {
        return className;
    }
}