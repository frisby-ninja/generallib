package net.ninjaworks.generallib;

import net.ninjaworks.generallib.util.Logger;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Allows for dynamic class creation during runtime.
 * Basically just a normal class, but with an associated file.
 * You can modify the file through the instance during runtime,
 * and the class will then attempt to auto-recompile.
 */
public class FileBasedClass {

    private boolean compiled = false;
    private final String name;
    private final String pkg;
    private final String path;
    private MethodInfo[] methods;
    private final Holder holder;
    private final TypeParameter[] typeParams;
    private Object instance = null;
    private Class<?> clazz;
    private URLClassLoader classLoader;
    private Constructor<?> previouslyUsedConstructor;
    private Object[] previouslyUsedConstructorParams;
    private Class<?>[] previouslyUsedConstructorParamClasses;
    private static final PrintStream defaultPrintStream = System.out;
    ArrayManipulator<MethodInfo> methodInfoArrayManipulator = new ArrayManipulator<>();

    /**
     * Creates a new FileBasedClass object based off of the given class.
     * The resulting object will be named the same as the original class, with "_FileBased" at the end.
     * @param clazz The class to base the object off of
     * @param path The path to the folder which will contain the file (will be created if non-existent). MUST NOT CONTAIN THE FILE ITSELF
     * @return The created object
     */
    public static FileBasedClass fromClass(Class<?> clazz, String path) {
        String name = clazz.getName();
        String[] split = name.split("\\.");
        StringBuilder pkgBuilder = new StringBuilder();
        pkgBuilder.append(split[0]);
        for(int i = 1; i < split.length - 1; i++) {
            pkgBuilder.append(".").append(split[i]);
        }
        name = generateNameFrom(removePackageAndGetImport(name)[0], pkgBuilder.toString());
        //TODO Read lines
        return new FileBasedClass(name, pkgBuilder.toString(), new String[0],
                pkgBuilder.toString(), new Holder(clazz));
    }

    private static String generateNameFrom(String original, String newPkg) {
        String name = original + "_FileBased";
        try {
            Class.forName(newPkg + "." + name);
        } catch (ClassNotFoundException e) {
            return name;
        }
        String baseName = name;
        int append = 0;
        while(true) {
            name = baseName + "_" + append;
            try {
                Class.forName(newPkg + "." + name);
            } catch (ClassNotFoundException e) {
                return name;
            }
            append++;
        }
    }

    private FileBasedClass(String name, String pkg, String[] lines, String path, Holder holder) {
        this.name = name;
        this.pkg = pkg;
        this.path = path;
        this.holder = holder;
        this.typeParams = new TypeParameter[0];
    }

    /**
     * Creates a new file, from which the class will be compiled.
     * @param name The name of the class, THIS IS AN IMPORTANT PARAMETER
     * @param path The path to the folder which will contain the file (will be created if non-existent). MUST NOT CONTAIN THE FILE ITSELF
     * @param methods The info of the methods to populate the class with (optional, can be modified later)
     */
    public FileBasedClass(String name, String pkg, String path, TypeParameter[] typeParams, MethodInfo... methods) {
        this.name = name;
        this.pkg = pkg;
        this.path = path;
        this.methods = methods;
        this.holder = null;
        if(typeParams == null) {
            typeParams = new TypeParameter[0];
        }
        this.typeParams = typeParams;
    }

    /**
     * Run a method within the class.
     * If the class is not compiled, it will try to compile itself automatically.
     * Equivalent to 'runMethod([methodName], true, [params])'
     * @param methodName The name of the method ; if the constructor is desired, put "/constructor".
     *                   The stored instance will then become the result.
     * @param params The parameters to run the method with
     * @return The return value of the method, an exception if it fails
     */
    public Object runMethod(String methodName, Object... params) {
        return runMethod(methodName, true, params);
    }

    /**
     * Runs the constructor with the given params, and stores the resulting instance.
     * Equivalent to 'newInstance(true, [params])'
     * @param params The parameters to run the constructor with, with the type parameters
     *               at the beginning (if needed)
     * @return The resulting instance
     */
    public Object newInstance(Object... params) {
        return newInstance(true, params);
    }

    /**
     * Runs the constructor with the given params, and stores the resulting instance.
     * @param print Whether to print the output of the constructor
     * @param params The parameters to run the constructor with, with the type parameters
     *               at the beginning (if needed)
     * @return The resulting instance
     */
    public Object newInstance(boolean print, Object... params) {
        if(params == null) {
            params = new Object[0];
        }
        Object[] typeParamVals = new Object[0];
        ArrayManipulator<Object> objectArrayManipulator = new ArrayManipulator<>();
        for (int i = 0; i < typeParams.length; i++) {
            if(params.length - 1 >= i) {
                objectArrayManipulator.setArray(typeParamVals);
                typeParamVals = objectArrayManipulator.addElement(params[i]);
            }
        }
        Class<?>[] parameters = new Class[0];
        ArrayManipulator<Class<?>> classArrayManipulator = new ArrayManipulator<>();
        if(typeParamVals.length > 0) {
            for (int i = typeParamVals.length - 1; i < params.length; i++) {
                Object object = params[i];
                classArrayManipulator.setArray(parameters);
                if (object != null) {
                    parameters = classArrayManipulator.addElement(object.getClass());
                } else {
                    parameters = classArrayManipulator.addElement(null);
                }
            }
        }
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(parameters);
            if (!print) {
                System.setOut(new PrintStream(OutputStream.nullOutputStream()));
            }
            instance = constructor.newInstance(params);
            if (!print) {
                System.setOut(defaultPrintStream);
            }
            previouslyUsedConstructor = constructor;
            previouslyUsedConstructorParams = params;
            previouslyUsedConstructorParamClasses = parameters;
            return instance;
        } catch(Exception e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
        return null;
    }

    /**
     * Run a method within the class.
     * If the class is not compiled, it will try to compile itself automatically.
     * @param methodName The name of the method
     * @param print Whether to print the method's output to the console
     * @param params The parameters to run the method with
     * @return The return value of the method, an exception if it fails
     */
    public Object runMethod(String methodName, boolean print, Object... params) {
        if(compiled) {
            try {
                Class<?>[] parameters = new Class[0];
                ArrayManipulator<Class<?>> classArrayManipulator = new ArrayManipulator<>();
                for (Object object : params) {
                    classArrayManipulator.setArray(parameters);
                    if(object != null) {
                        parameters = classArrayManipulator.addElement(object.getClass());
                    } else {
                        parameters = classArrayManipulator.addElement(null);
                    }
                }
                Method method = clazz.getDeclaredMethod(methodName, parameters);
                if(!print) {
                    System.setOut(new PrintStream(OutputStream.nullOutputStream()));
                }
                Object obj = method.invoke(instance, params);
                if(!print) {
                    System.setOut(defaultPrintStream);
                }
                return obj;
            } catch (Exception e) {
                Logger.GENERAL_LIB_LOGGER.printException(e);
                return e;
            }
        }
        if(compile()) {
            runMethod(methodName, print, params);
        }
        return null;
    }

    /**
     * Gets all the declared methods in the class file.
     * @return The declared methods
     */
    public Method[] getDeclaredMethods() {
        if(clazz != null) {
            return clazz.getDeclaredMethods();
        }
        return null;
    }

    /**
     * Adds the given methods to the class file and recompiles the class when finished.
     * Also tries to update the instance using the previously-used constructor and parameters.
     * @return The result of the recompilation
     */
    public boolean addMethods(MethodInfo... methods) {
        if(methods != null) {
            for (MethodInfo method : methods) {
                methodInfoArrayManipulator.setArray(this.methods);
                this.methods = methodInfoArrayManipulator.addElement(method);
            }
            write();
            boolean success = compile();
            if(previouslyUsedConstructor != null) {
                try {
                    instance = newInstance(false, previouslyUsedConstructorParams);
                } catch (Exception e) {
                    Logger.GENERAL_LIB_LOGGER.printException(e);
                }
            }
            return success;
        }
        return true;
    }

    /**
     * Removes the given methods (found from the given info) from the class file
     * and recompiles the class when finished.
     * Also tries to update the instance using the previously-used constructor and parameters.
     * @return The result of the recompilation
     */
    public boolean removeMethods(ReducedMethodInfo... methods) {
        boolean hasMatch;
        boolean canCheckForConstructor = true;
        boolean shouldCheckConstructor = false;
        boolean constructorDestroyed = false;
        if(previouslyUsedConstructor == null) {
            canCheckForConstructor = false;
        }
        for(ReducedMethodInfo reducedMethodInfo : methods) {
            hasMatch = true;
            String nm = reducedMethodInfo.name();
            if(canCheckForConstructor) {
                if (nm.equals("/constructor") && previouslyUsedConstructorParams != null) {
                    shouldCheckConstructor = true;
                }
            }
            String name = reducedMethodInfo.name();
            Class<?>[] params = reducedMethodInfo.params();
            for (MethodInfo method : this.methods) {
                if(shouldCheckConstructor) {
                    if(method instanceof ConstructorInfo && Arrays.equals(method.getParams(), previouslyUsedConstructorParams)) {
                        constructorDestroyed = true;
                    }
                    shouldCheckConstructor = false;
                }
                if(method.getName().equals(name)) {
                    AssociatedObjects[] methodParams = method.getParams();
                    if(params == null) {
                        if(methodParams == null) {
                            methodInfoArrayManipulator.setArray(this.methods);
                            this.methods = methodInfoArrayManipulator.removeElementAt(methodInfoArrayManipulator.indexOf(method));
                        }
                    } else {
                        for(int i = 0; i < methodParams.length; i++) {
                            AssociatedObjects param = methodParams[i];
                            if(!param.obj1().getClass().equals(params[i])) {
                                hasMatch = false;
                                break;
                            }
                        }
                        if(hasMatch) {
                            methodInfoArrayManipulator.setArray(this.methods);
                            this.methods = methodInfoArrayManipulator.removeElementAt(methodInfoArrayManipulator.indexOf(method));
                        }
                    }
                }
            }
        }
        write();
        boolean success = compile();
        if(!constructorDestroyed) {
            try {
                instance = newInstance(false, previouslyUsedConstructorParams);
            } catch (Exception e) {
                Logger.GENERAL_LIB_LOGGER.printException(e);
            }
        }
        return success;
    }

    /**
     * Compiles the class for later use, or recompiles if it is already compiled.
     * MUST BE DONE BEFORE RUNNING ANY METHOD.
     * @return Whether the compilation was successful
     */
    public boolean compile() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null,
                path + "/" + pkg.replace(".", "/") + "/" + name + ".java");
        File dir = new File(path);

        try {
            classLoader = URLClassLoader.newInstance(new URL[]{dir.toURI().toURL()});
            clazz = Class.forName(pkg + "." + name, true, classLoader);
            instance = null;
        } catch (Exception e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
        compiled = (result == 0);
        return compiled;
    }

    /**
     * Writes the file for the class, rewrites it if it already existed.
     */
    public void write() {
        String filePath = (path.replace("\\", "/") + "/" + pkg.replace(".", "/") +
                "/" + name + ".java").replace("/", "\\");
        StringBuilder mainBuilder = new StringBuilder();
        StringBuilder importsBuilder = new StringBuilder();
        File file = new File(filePath);
        file.mkdirs();
        file.delete();
        try {
            Files.createFile(file.toPath());
            importsBuilder.append("package ").append(pkg).append(";").append("\n\n");
            String typeParamString = null;
            if(holder != null && holder.clazz != null) {
                String[] importsAndTypeParams = getTypeParamsAndExtraImportsFromClass(holder.clazz);
                typeParamString = importsAndTypeParams[0];
                for (int i = 1; i < importsAndTypeParams.length; i++) {
                    addImportIfNeeded(importsBuilder, importsAndTypeParams[i]);
                }
            } else {
                if(typeParams.length > 0) {
                    StringBuilder builder = new StringBuilder("<");
                    String[] lines = typeParams[0].toLines();
                    builder.append(lines[0]);
                    if(lines.length > 1) {
                        addImportIfNeeded(importsBuilder, lines[1]);
                    }
                    for (int i = 1; i < typeParams.length; i++) {
                        lines = typeParams[i].toLines();
                        builder.append(", ").append(lines[0]);
                        if(lines.length > 1) {
                            addImportIfNeeded(importsBuilder, lines[1]);
                        }
                    }
                    builder.append(">");
                    typeParamString = builder.toString();
                }
            }
            mainBuilder.append("public class ").append(name);
            if(typeParamString != null) {
                mainBuilder.append(typeParamString);
            }
            mainBuilder.append(" {\n\n");
            for(int i = 0; i < methods.length; i++) {
                MethodInfo method = methods[i];
                AssociatedObjects lines = method.toLines();
                String[] actualLines = (String[]) lines.obj2();
                if(actualLines != null) {
                    for (String line : actualLines) {
                        mainBuilder.append("    ").append(line).append("\n");
                    }
                    if(i != methods.length - 1) {
                        mainBuilder.append("\n");
                    }
                }
                String[] methodImports = (String[]) lines.obj1();
                if(methodImports != null) {
                    for (String string : methodImports) {
                        addImportIfNeeded(importsBuilder, string);
                    }
                }
            }
            importsBuilder.append("\n");
            mainBuilder.append("}");
            FileWriter writer = new FileWriter(filePath);
            writer.write(importsBuilder.toString());
            writer.write(mainBuilder.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            Logger.GENERAL_LIB_LOGGER.printException(e);
        }
    }

    private void addImportIfNeeded(StringBuilder builder, String toAdd) {
        if(!builder.toString().contains(toAdd)) {
            builder.append(toAdd).append("\n");
        }
    }

    public static String[] getTypeParamsAndExtraImportsFromClass(Class<?> clazz) {
        StringBuilder builder = new StringBuilder();
        String[] imports = new String[0];
        ArrayManipulator<String> stringArrayManipulator = new ArrayManipulator<>();
        TypeVariable<? extends Class<?>>[] typeParams = clazz.getTypeParameters();
        if(typeParams.length > 0) {
            builder.append("<");
            builder.append(typeParams[0].getName());
            String[] removedPackageAndImport = removePackageAndGetImport(typeParams[0].getBounds()[0].getTypeName());
            String extendingClass = removedPackageAndImport[0];
            String[] objClassName = Object.class.getName().split("\\.");
            if(extendingClass != null && !extendingClass.equals(objClassName[objClassName.length - 1])) {
                builder.append(" extends ").append(extendingClass);
                stringArrayManipulator.setArray(imports);
                imports = stringArrayManipulator.addElement(removedPackageAndImport[1]);
            }
            if(typeParams.length > 1) {
                for(int i = 1; i < typeParams.length; i++) {
                    TypeVariable<? extends Class<?>> typeParam = typeParams[i];
                    builder.append(", ");
                    builder.append(typeParam.getName());
                    removedPackageAndImport = removePackageAndGetImport(typeParam.getBounds()[0].getTypeName());
                    extendingClass = removedPackageAndImport[0];
                    if(extendingClass != null && !extendingClass.equals(objClassName[objClassName.length - 1])) {
                        builder.append(" extends ").append(extendingClass);
                        stringArrayManipulator.setArray(imports);
                        imports = stringArrayManipulator.addElement(removedPackageAndImport[1]);
                    }
                }
            }
            builder.append(">");
        }
        String[] toReturn = {builder.toString()};
        stringArrayManipulator.setArray(toReturn);
        if(imports != null) {
            stringArrayManipulator.setArray(toReturn);
            return stringArrayManipulator.joinToArray(imports, true);
        }
        return toReturn;
    }

    public static String[] removePackageAndGetImport(String string) {
        String[] strings = new String[2];
        String[] splitByPeriod = string.split("\\.");
        if(splitByPeriod.length > 0) {
            String justClass = splitByPeriod[splitByPeriod.length - 1];
            strings[0] = justClass;
        }

        strings[1] = "import " + string + ";";
        return strings;
    }

    public Object getInstance() {
        return instance;
    }

    public boolean isCompiled() {
        return compiled;
    }

    private record Holder(Class<?> clazz) {

    }
}