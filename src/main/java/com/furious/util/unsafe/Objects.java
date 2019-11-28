package com.furious.util.unsafe;

import java.lang.reflect.Field;

import com.furious.util.Throwables;

/**
 * Object util
 */
public abstract class Objects {

    private Objects() {
    }

    /**
     * fill attribute in target class;
     * source->target
     */
    public static void populate(Object source, Object target) {
        //source target maybe NPE
        Field[] fields = fields(source);
        String name;
        FieldOffset sFo;
        FieldOffset tFo;
        Field f;
        for (Field field : fields) {
            name = field.getName();
            sFo = new FieldOffset(source, field);
            Object v = sFo.get();

            f = field(target, name);
            if (f == null) {
                //not found field
                continue;
            }
            tFo = new FieldOffset(target, f);

            tFo.put(v);
        }
    }

    private static Field[] fields(Object obj) {
        return obj.getClass().getDeclaredFields();
    }

    private static Field field(Object obj, String name) {
        Field f = null;
        try {
            f = obj.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Throwables.raise(e);
        }
        return f;
    }
}
