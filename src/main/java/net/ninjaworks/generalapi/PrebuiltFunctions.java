package net.ninjaworks.generalapi;

import net.ninjaworks.generalapi.util.PropertyInfusedString;

public class PrebuiltFunctions {

    public static class PrintLineFunction extends Function {

        public PrintLineFunction(String line2Print) {
            super(new PropertyInfusedString[]{
                    new PropertyInfusedString(
                            "System.out.println(%s);", line2Print
                    )
            }, new PropertyInfusedString[0]);
        }
    }
}