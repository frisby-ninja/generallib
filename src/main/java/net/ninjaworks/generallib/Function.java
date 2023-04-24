package net.ninjaworks.generallib;

import net.ninjaworks.generallib.util.PropertyInfusedString;

public class Function {

    public final String[] lines;
    public final String[] imports;

    public Function(String[] lines, String[] imports) {
        this.lines = lines;
        this.imports = imports;
    }

    public Function(PropertyInfusedString[] lines, PropertyInfusedString[] imports) {
        String[] linesStrings = new String[0];
        String[] importsStrings = new String[0];
        ArrayManipulator<String> stringArrayManipulator = new ArrayManipulator<>();
        for(PropertyInfusedString str : lines) {
            stringArrayManipulator.setArray(linesStrings);
            linesStrings = stringArrayManipulator.addElement(str.toString());
        }
        for(PropertyInfusedString str : imports) {
            stringArrayManipulator.setArray(importsStrings);
            importsStrings = stringArrayManipulator.addElement(str.toString());
        }
        this.lines = linesStrings;
        this.imports = importsStrings;
    }

    public static class Builder {

        private String[] lines = new String[0];
        private String[] imports = new String[0];
        private final ArrayManipulator<String> stringArrayManipulator = new ArrayManipulator<>();

        public Builder addPrebuiltFunction(Function prebuiltFunction) {
            for(String line : prebuiltFunction.lines) {
                stringArrayManipulator.setArray(lines);
                lines = stringArrayManipulator.addElement(line);
            }
            for(String importLn : prebuiltFunction.imports) {
                stringArrayManipulator.setArray(imports);
                imports = stringArrayManipulator.addElement(importLn);
            }
            return this;
        }

        public Builder addLine(String line) {
            stringArrayManipulator.setArray(lines);
            lines = stringArrayManipulator.addElement(line);
            return this;
        }

        public Builder addLine(PropertyInfusedString line) {
            stringArrayManipulator.setArray(lines);
            lines = stringArrayManipulator.addElement(line.toString());
            return this;
        }

        public Function build() {
            return new Function(lines, imports);
        }
    }
}