package com.furious.util.unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.furious.util.Throwables;

/**
 * reflection tools
 */
public class Reflections {

    /**
     * 设置对象中属性的值
     *
     * @param object 属性所在对象，若field为静态属性，则传null
     * @param field  属性
     * @param value  所设值
     */
    public static void setField(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            Field mf = Field.class.getDeclaredField("modifiers");
            mf.setAccessible(true);
            mf.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(object, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Throwables.raise(e);
        }
    }

}
