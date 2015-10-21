package org.wit.ff.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by F.Fang on 2015/9/17.
 * Version :2015/9/17
 */
public final class ClassUtil {

    private static final Map<String, Class<?>> primitivesToClasses;

    private ClassUtil() {
    }

    public static Class<?> getWrapperClassType(String typeStr) {
        return primitivesToClasses.get(typeStr);
    }

    static {
        HashMap<String, Class<?>> tmp = new HashMap<>();
        tmp.put("boolean", Boolean.TYPE);
        tmp.put("Boolean", Boolean.TYPE);
        tmp.put("int", Integer.TYPE);
        tmp.put("Integer", Integer.TYPE);
        tmp.put("char", Character.TYPE);
        tmp.put("Character", Character.TYPE);
        tmp.put("short", Short.TYPE);
        tmp.put("Short", Short.TYPE);
        tmp.put("int", Integer.TYPE);
        tmp.put("Integer", Integer.TYPE);
        tmp.put("long", Long.TYPE);
        tmp.put("Long", Long.TYPE);
        tmp.put("float", Float.TYPE);
        tmp.put("Float", Float.TYPE);
        tmp.put("double", Double.TYPE);
        tmp.put("Double", Double.TYPE);
        tmp.put("void", Void.TYPE);
        tmp.put("Void", Void.TYPE);

        primitivesToClasses = Collections.unmodifiableMap(tmp);
    }
}
