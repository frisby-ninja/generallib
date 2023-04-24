package net.ninjaworks.generalapi;

import net.ninjaworks.generalapi.util.JsonPart;
import net.ninjaworks.generalapi.util.Logger;
import net.ninjaworks.generalapi.util.SysColors;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class Test {

    public static void main(String[] args) {
        new Test().doJson();
    }

    public void doJson() {
        try {
            String pathStr = System.getProperty("user.home").replace("\\", "/") +
                    "/AppData/Roaming/.gadata/";
            Path path = Path.of(pathStr);
            File jsonFile = NetUtils.downloadFileFromURLWithTMP(
                    new URL("https://raw.githubusercontent.com/frisby-ninja/goat-info/main/releases.json"),
                    path);
            Json json = new Json(jsonFile);
            json.getItem(new JsonPart("releases", new JsonPart[0], JsonPart.Type.ELEMENT));
        } catch (Exception e) {
            Logger.GENERAL_API_LOGGER.printException(e);
        }
    }

    public void doFBC() {
        String path = System.getProperty("user.home").replace("\\", "/") + "/AppData/Roaming/.clsdata/";
        FileBasedClass fileBasedClass = new FileBasedClass(
                "Test_1", "net.ninjaworks.generalapi", path, new TypeParameter[0],

                new MethodInfo("calc", Modifier.PUBLIC, true, new Function[]{
                        new PrebuiltFunctions.PrintLineFunction("Hi!"),
                        new Function(new String[]{"return null;"}, new String[0])
                }, Test.class),

                new ConstructorInfo("Test_1", Modifier.PUBLIC, new Function[]{
                        new PrebuiltFunctions.PrintLineFunction("Hello!")
                })
        );
        fileBasedClass.write();
        fileBasedClass.compile();
        fileBasedClass.newInstance();
        fileBasedClass.runMethod("calc");
        fileBasedClass.addMethods(
                new MethodInfo("calc_1", Modifier.PUBLIC, true,
                        new Function[]{
                                new PrebuiltFunctions.PrintLineFunction("Hi_1!"),
                                new Function(new String[]{"return null;"}, new String[0])
                        },
                        Test.class)
        );
        fileBasedClass.runMethod("calc_1");
    }
}