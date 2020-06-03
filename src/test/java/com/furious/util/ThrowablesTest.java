package com.furious.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ThrowablesTest {

    @Test
    public void testSneakyThrow() {
        assertThrows(NullPointerException.class, () -> Throwables.sneakyThrow(new NullPointerException()));
    }
}