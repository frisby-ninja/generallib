package net.ninjaworks.generallib;

public class TypeParameter {

    private final String name;
    private final Class<?> extendingClass;

    public TypeParameter(String name) {
        this.name = name;
        this.extendingClass = Object.class;
    }

    public TypeParameter(String name, Class<?> extendingClass) {
        this.name = name;
        this.extendingClass = extendingClass;
    }

    public String[] toLines() {
        if(extendingClass == null) {
            return new String[]{name};
        }
        if(extendingClass.equals(Object.class)) {
            return new String[]{name};
        }
        String[] removedPackageAndImport = FileBasedClass.removePackageAndGetImport(extendingClass.getName());
        return new String[]{
                name + " extends " + removedPackageAndImport[0],
                removedPackageAndImport[1]
        };
    }
}