package com.furious.util.unsafe;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * @see sun.misc.Unsafe
 * http://www.docjar.com/html/api/sun/misc/Unsafe.java.html
 */
public abstract class Unsafes {

    private static final Unsafe unsafe;

    private Unsafes() {
        //private
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }

    static {
        Field f;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //...
}
