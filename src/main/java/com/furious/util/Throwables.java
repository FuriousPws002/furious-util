package com.furious.util;

import com.furious.util.unsafe.Unsafes;

public abstract class Throwables {

    private Throwables() {
    }

    public static void raise(Throwable t) {
        Unsafes.getUnsafe().throwException(t);
    }

}
