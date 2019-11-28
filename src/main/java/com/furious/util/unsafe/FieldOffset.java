package com.furious.util.unsafe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FieldOffset {

    private Object object;
    private long offset;
    private Class type;

    FieldOffset(Object object, Field field) {
        this.object = object;
        this.offset = Unsafes.getUnsafe().objectFieldOffset(field);
        this.type = field.getType();
    }

    private static Map<Class, Integer> types = new HashMap<>();

    static {
        types.put(byte.class, 0);
        types.put(short.class, 1);
        types.put(int.class, 2);
        types.put(long.class, 3);
        types.put(float.class, 4);
        types.put(double.class, 5);
        types.put(boolean.class, 6);
        types.put(char.class, 7);
    }

    private HandleGet[] gets = {
            () -> Unsafes.getUnsafe().getByte(object, offset),
            () -> Unsafes.getUnsafe().getShort(object, offset),
            () -> Unsafes.getUnsafe().getInt(object, offset),
            () -> Unsafes.getUnsafe().getLong(object, offset),
            () -> Unsafes.getUnsafe().getFloat(object, offset),
            () -> Unsafes.getUnsafe().getDouble(object, offset),
            () -> Unsafes.getUnsafe().getBoolean(object, offset),
            () -> Unsafes.getUnsafe().getChar(object, offset),
    };

    private HandlePut[] puts = {
            (v) -> Unsafes.getUnsafe().putByte(object, offset, (byte) v),
            (v) -> Unsafes.getUnsafe().putShort(object, offset, (short) v),
            (v) -> Unsafes.getUnsafe().putInt(object, offset, (int) v),
            (v) -> Unsafes.getUnsafe().putLong(object, offset, (long) v),
            (v) -> Unsafes.getUnsafe().putFloat(object, offset, (float) v),
            (v) -> Unsafes.getUnsafe().putDouble(object, offset, (double) v),
            (v) -> Unsafes.getUnsafe().putBoolean(object, offset, (boolean) v),
            (v) -> Unsafes.getUnsafe().putChar(object, offset, (char) v),
    };


    public Object get() {
        Integer index = types.get(type);
        //assert index range
        return gets[index].apply();
    }

    public void put(Object object) {
        Integer index = types.get(type);
        puts[index].apply(object);
    }

    private interface HandleGet {
        Object apply();
    }

    private interface HandlePut {
        void apply(Object value);
    }

}
