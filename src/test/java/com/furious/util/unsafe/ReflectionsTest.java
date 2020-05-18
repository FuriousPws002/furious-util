package com.furious.util.unsafe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class ReflectionsTest {

    private static class TestStaticField {
        private static final Integer val = 1;

        private TestStaticField() {
        }
    }

    @Test
    public void testSetStaticField() throws Exception {
        Field field = TestStaticField.class.getDeclaredField("val");
        Reflections.setField(null, field, 2);
        assertEquals(2, TestStaticField.val);
    }
}