package net.ninjaworks.generallib.util;

public class PropertyInfusedString {

    private final String string;

    public PropertyInfusedString(String string, String... replacements) {
        StringBuilder builder = new StringBuilder();
        boolean isInEscapeChar = false;
        int indexInReplacements = 0;
        char[] chars = string.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            char chr = chars[i];
            if(isInEscapeChar) {
                if(chr == 's') {
                    if(replacements != null && indexInReplacements < replacements.length) {
                        builder.append('"').append(replacements[indexInReplacements]).append('"');
                        indexInReplacements++;
                    }
                    i++;
                    chr = chars[i];
                    isInEscapeChar = false;
                }
            }
            if(chr == '%') {
                isInEscapeChar = true;
            }
            if(!isInEscapeChar) {
                builder.append(chr);
            }
        }
        this.string = builder.toString();
    }

    @Override
    public String toString() {
        return string;
    }
}