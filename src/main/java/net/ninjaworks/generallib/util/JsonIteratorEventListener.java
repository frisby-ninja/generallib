package net.ninjaworks.generallib.util;

public interface JsonIteratorEventListener {

    int onArrayDeclaration(int withinStringIndex, String header, String withinBrackets);
    int onCurlyDeclaration(int withinStringIndex, String header, String withinBrackets);
    int onStringDeclaration(int withinStringIndex, String header, String string);
}