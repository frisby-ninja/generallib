package net.ninjaworks.generalapi.util;

import net.ninjaworks.generalapi.ArrayManipulator;
import net.ninjaworks.generalapi.AssociatedObjects;
import net.ninjaworks.generalapi.IndexableMap;
import net.ninjaworks.generalapi.Json;

public class JsonIteratorHelper {

    public static class JsonIteratorSqrBracketsHelper implements JsonIteratorEventListener {

        private final ArrayManipulator<AssociatedObjects> arrayManipulator = new ArrayManipulator<>();
        public AssociatedObjects[] objects = new AssociatedObjects[0];
        private final Json instance;

        public JsonIteratorSqrBracketsHelper(Json instance) {
            this.instance = instance;
        }

        @Override
        public int onArrayDeclaration(int withinStringIndex, String header, String withinBrackets) {
            arrayManipulator.setArray(objects);
            objects = arrayManipulator.addElement(new AssociatedObjects(header, instance.getWithinSquareBrackets(withinBrackets)));
            //The '+ 1' is to account for the ']' character at the end.
            return withinStringIndex + withinBrackets.length() + 1;
        }

        @Override
        public int onCurlyDeclaration(int withinStringIndex, String header, String withinBrackets) {
            arrayManipulator.setArray(objects);
            objects = arrayManipulator.addElement(new AssociatedObjects(header, instance.getWithinCurlyBrackets(withinBrackets)));
            //The '+ 1' is to account for the ']' character at the end.
            return withinStringIndex + withinBrackets.length() + 1;
        }

        public int onStringDeclaration(int withinStringIndex, String header, String string) {
            objects = arrayManipulator.addElement(new AssociatedObjects(header, string));
            //The '+ 1' is to account for the ']' character at the end.
            return withinStringIndex + string.length() + 1;
        }
    }

    public static class JsonIteratorCurlyBracketsHelper implements JsonIteratorEventListener {

        private final Json instance;
        public final IndexableMap<String, Object> map = new IndexableMap<>();

        public JsonIteratorCurlyBracketsHelper(Json instance) {
            this.instance = instance;
        }

        @Override
        public int onArrayDeclaration(int withinStringIndex, String header, String withinBrackets) {
            map.put(header, instance.getWithinSquareBrackets(withinBrackets));
            //The '+ 1' is to account for the ']' character at the end.
            return withinStringIndex + withinBrackets.length() + 1;
        }

        @Override
        public int onCurlyDeclaration(int withinStringIndex, String header, String withinBrackets) {
            map.put(header, instance.getWithinCurlyBrackets(withinBrackets));
            //The '+ 1' is to account for the '}' character at the end.
            return withinStringIndex + withinBrackets.length() + 1;
        }

        @Override
        public int onStringDeclaration(int withinStringIndex, String header, String string) {
            map.put(header, string);
            //The '+ 1' is to account for the '"' character at the end.
            return withinStringIndex + string.length() + 1;
        }
    }
}