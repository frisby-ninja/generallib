package net.ninjaworks.generallib.util;

import net.ninjaworks.generallib.ArrayManipulator;
import net.ninjaworks.generallib.AssociatedObjects;
import net.ninjaworks.generallib.IndexableMap;
import net.ninjaworks.generallib.Json;

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

        private JsonPart[] tree = new JsonPart[0];
        private final ArrayManipulator<JsonPart> jsonPartArrayManipulator = new ArrayManipulator<>();

        public Builder() {

        }

        public Builder addTreeElement(ReducedJsonPart part) {
            jsonPartArrayManipulator.setArray(tree);
            tree = jsonPartArrayManipulator.addElement(new JsonPart(part.header, tree, part.type));
            return this;
        }

        public JsonPart build() {
            return new JsonPart(tree[tree.length - 1].header, convertTree(), tree[tree.length - 1].type);
        }

        private JsonPart[] convertTree() {
            JsonPart[] parts = new JsonPart[0];
            for (int i = 0; i < tree.length - 1; i++) {
                JsonPart part = tree[i];
                jsonPartArrayManipulator.setArray(parts);
                parts = jsonPartArrayManipulator.addElement(part);
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
}