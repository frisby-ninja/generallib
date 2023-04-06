package net.ninjaworks.generalapi.util;

import net.ninjaworks.generalapi.ArrayManipulator;
import net.ninjaworks.generalapi.AssociatedObjects;
import net.ninjaworks.generalapi.IndexableMap;
import net.ninjaworks.generalapi.Json;

public class JsonPart {
    public final String header;
    public final JsonPart[] tree;
    public final Type type;

    public JsonPart(String header, JsonPart[] tree, Type type) {
        this.header = header;
        this.tree = tree;
        this.type = type;
    }

    public Object getInJson(Json json) {
        return json.getItem(this);
    }

    public static class Builder {

        private ReducedJsonPart[] tree = new ReducedJsonPart[0];
        private final ArrayManipulator<JsonPart> arrayManipulator = new ArrayManipulator<>();
        private final String header;
        private final Type type;

        public Builder(String header, Type type) {
            this.header = header;
            this.type = type;
        }

        public Builder within() {
            return this;
        }

        public JsonPart build() {
            return new JsonPart(header, convertTree(), type);
        }

        private JsonPart[] convertTree() {
            JsonPart[] parts = new JsonPart[0];
            JsonPart[] previousParts = new JsonPart[0];
            for(int i = 0; i < tree.length; i++) {
                ReducedJsonPart part = tree[i];
                arrayManipulator.setArray(parts);
                parts = arrayManipulator.addElement(new JsonPart(part.header, previousParts, part.type));
                previousParts = parts;
            }
            return parts;
        }

        private record ReducedJsonPart(String header, Type type) {

        }
    }

    public enum Type {
        ELEMENT,
        ARRAY,
        OBJECT_DECLARATION;

        public static Type fromObject(Object object) {
            if(object instanceof IndexableMap) {
                return ELEMENT;
            }
            if(object instanceof Object[]) {
                return ARRAY;
            }
            if(object instanceof AssociatedObjects) {
                return OBJECT_DECLARATION;
            }
            return null;
        }
    }

    public static record Associations (Object object, String enclosedStr) {

    }
}