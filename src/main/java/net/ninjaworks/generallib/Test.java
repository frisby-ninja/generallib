package net.ninjaworks.generallib;

import net.ninjaworks.generallib.util.ITest;

public class Test implements ITest {

    public static void main(String[] args) {
        String[] pkgSplit = ITest.class.getPackage().toString().replace("package ", "").split("\\.");
        String pkg = pkgSplit[0] + '.' + pkgSplit[1] + '.' + pkgSplit[2];
        System.out.println(pkg);
        //System.out.println(Arrays.toString(ClassUtils.getAllImplementationsOf(ITest.class)));
    }
}