package com.furious.util;

import com.furious.util.unsafe.Unsafes;

import lombok.Lombok;

public abstract class Throwables {

    private Throwables() {
    }

    public static void raise(Throwable t) {
        Unsafes.getUnsafe().throwException(t);
    }

    /**
     * @see Lombok#sneakyThrow(java.lang.Throwable)
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
