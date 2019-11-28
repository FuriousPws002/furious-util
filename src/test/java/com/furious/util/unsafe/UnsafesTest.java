package com.furious.util.unsafe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UnsafesTest {

    @Test
    void testGetUnsafe() {
        assertNotNull(Unsafes.getUnsafe());
    }
}