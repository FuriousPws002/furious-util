package com.furious.util.unsafe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lombok.Getter;
import lombok.Setter;

class FieldOffsetTest {

    private static class User {
        @Setter
        @Getter
        private int age;
    }

    @Test
    public void testGetAndPut() throws Exception {
        User from = new User();
        from.setAge(10);
        FieldOffset fromFo = new FieldOffset(from, from.getClass().getDeclaredField("age"));
        Object value = fromFo.get();

        User to = new User();
        FieldOffset toFo = new FieldOffset(to, to.getClass().getDeclaredField("age"));
        toFo.put(value);

        assertEquals(from.getAge(),to.getAge());
    }
}