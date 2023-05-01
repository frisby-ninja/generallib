package net.ninjaworks.generallib;

import net.ninjaworks.generallib.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Json utility class
 */
public class Json {

    private final ArrayManipulator<Object> objectArrayManipulator = new ArrayManipulator<>();
    private final StringManipulator stringManipulator = new StringManipulator();
    private final String fileString;
    private final IndexableMap<String, Object> fileMap;

    /**
     * Creates a new Json object from the given json file string.
     * @param file The file; This program assumes it is a FULL and valid json file
     * @return The created Json object
     */
    public static Json fromFile(File file) {
        StringBuilder builder = new StringBuilder();
        try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()) {
                builder.append(reader.nextLine());
            }
        } catch (FileNotFoundException ignored) {}
        return new Json(builder.toString());
    }

    @Override
    public String toString() {
        return fileString;
    }

    /**
     * Creates a new Json object from the given json file string.
     * @param fileString The file (converted to a string); This program assumes it is a FULL and valid json file
     */
    public Json(String fileString) {
        this.fileString = fileString;
        stringManipulator.setString(fileString);
        fileMap = getWithinCurlyBrackets(stringManipulator.getFromIndexUntilEndChar(1, '}', '{'));
    }

    /**
     * Gets the given json part within the json file.
     * @param item The item to get
     * @return The parsed item, or the last successful parsed one
     */
    @SuppressWarnings("all")
    public Object getItem(JsonPart item) {
        Object currentObj = fileMap;
        JsonPart.Type currentType = JsonPart.Type.ELEMENT;
        JsonPart.Type nextType;
        for(int i = 0; i < item.tree.length; i++) {
            JsonPart part = item.tree[i];
            nextType = item.tree[i].type;
            switch (currentType) {
                case ELEMENT: {
                    assert currentObj instanceof IndexableMap;
                    currentObj = ((IndexableMap<String, Object>) currentObj).get(part.header);
                }
                case ARRAY: {
                    assert currentObj instanceof AssociatedObjects[];
                    AssociatedObjects[] currentArray = (AssociatedObjects[]) currentObj;
                    objectArrayManipulator.setArray(currentArray);
                    currentObj = currentArray[objectArrayManipulator.indexOf(AssociatedObjects.getInstance(part.header, null))];
                }
                case OBJECT_DECLARATION: {
                    //Can't continue :/
                    break;
                }
            }
            currentType = nextType;
        }
        return switch (currentType) {
            case ELEMENT: {
                assert currentObj instanceof IndexableMap;
                yield ((IndexableMap<String, Object>) currentObj).get(item.header);
            }
            case ARRAY: {
                assert currentObj instanceof AssociatedObjects[];
                for(AssociatedObjects object : (AssociatedObjects[]) currentObj) {
                    if(object.obj1() == item.header) {
                        yield object.obj2();
                    }
                }
                yield currentObj;
            }
            case OBJECT_DECLARATION: {
                yield ((AssociatedObjects) currentObj).obj2();
            }
        };
    }

    /**
     * Gets all objects within an array (still in json format)
     * @param withinSqrBrackets The string within the square brackets of the array, EXCLUDING SAID BRACKETS
     * @return The parsed items within the brackets
     */
    public AssociatedObjects[] getWithinSquareBrackets(String withinSqrBrackets) {
        JsonIteratorHelper.JsonIteratorSqrBracketsHelper helper = new JsonIteratorHelper.JsonIteratorSqrBracketsHelper(this);
        privateIterator(withinSqrBrackets, helper);
        return helper.objects;
    }

    /**
     * Gets all objects within an element(?) (still in json format)
     * @param withinCurlyBrackets The string within the curly brackets of the element, EXCLUDING SAID BRACKETS
     * @return An indexable map containing the associated items within the curly brackets
     */
    public IndexableMap<String, Object> getWithinCurlyBrackets(String withinCurlyBrackets) {
        JsonIteratorHelper.JsonIteratorCurlyBracketsHelper helper = new JsonIteratorHelper.JsonIteratorCurlyBracketsHelper(this);
        privateIterator(withinCurlyBrackets, helper);
        return helper.map;
    }

    private void privateIterator(String string, JsonIteratorEventListener eventListener) {
        Character[] charsNotAppend = new Character[]{'{', '}', ',', ':', '[', ']', '"'};
        ArrayManipulator<Character> chars2NotAppend = new ArrayManipulator<>(charsNotAppend);
        char[] chars = string.replace(" ", "").replace("\n", "").toCharArray();
        StringBuilder headerBuilder = new StringBuilder();
        for(int i = 0; i < chars.length; i++) {
            char chr = chars[i];
            if(chr == ':') {
                //We can trigger an event; but which one?
                stringManipulator.setString(string);
                i = switch(chars[i + 1]) {
                    case '{' -> eventListener.onCurlyDeclaration(i + 2, headerBuilder.toString(),
                            stringManipulator.getFromIndexUntilEndChar(i + 2, '}', '{'));
                    case '"' -> eventListener.onStringDeclaration(i + 2, headerBuilder.toString(),
                            stringManipulator.getFromIndexUntilEndChar(i + 2, '"', ' '));
                    case '[' -> eventListener.onArrayDeclaration(i + 2, headerBuilder.toString(),
                            stringManipulator.getFromIndexUntilEndChar(i + 2, '}', '{'));
                    default -> doNothing(i + 2);
                };
                Logger.GENERAL_LIB_LOGGER.debug(String.valueOf(chars[i]));
            }
            chr = chars[i];
            if(!chars2NotAppend.arrayContainsElement(chr)) {
                headerBuilder.append(chr);
            }
        }
    }

    private int doNothing(int currentIndex) {return currentIndex + 1;}
}