package de.tudortmund.cs.iltis.utils.general.viewcopier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CloneHelper {
    public static Object clone(Object obj) throws IllegalAccessException {
        if (obj == null) return null;

        Method cloneMethod = null;
        try {
            cloneMethod = obj.getClass().getMethod("clone");
        } catch (NoSuchMethodException e) {
            throw new IllegalAccessException("Object has no clone method.");
        }

        try {
            return cloneMethod.invoke(obj);
        } catch (InvocationTargetException e) {
            throw new IllegalAccessException("Failed to invoke clone method.");
        }
    }
}
