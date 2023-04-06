package net.ninjaworks.generalapi.util;

public class StringManipulator {

    private String string;

    public StringManipulator() {
        this.string = null;
    }

    public StringManipulator(String string) {
        this.string = string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getFromIndexUntilEndChar(int fromIndex, char untilChar, char untilCharOpposite) {
        if(string == null) {
           return null;
        }
        char[] chars = string.toCharArray();
        StringBuilder builder = new StringBuilder();
        boolean inString = false;
        int amountOfChars = 0;
        for(int i = fromIndex; i < chars.length; i++) {
            char chr = chars[i];
            if(chr == '"') {
                inString = !inString;
            }
            if(chr == untilChar && !inString) {
                if(amountOfChars == 0) {
                    break;
                }
                amountOfChars--;
            }
            if(chr == untilCharOpposite && !inString) {
                amountOfChars++;
            }
            builder.append(chr);
        }
        return builder.toString();
    }
}