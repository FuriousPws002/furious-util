package com.furious.util.unsafe;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.Data;

class ObjectsTest {

    @Data
    private class Domain1 {
        private String username;
        private int age;
    }

    @Data
    private class Domain2 {
        private byte b;
        private short s;
        private int i = 10;
        private long l;
        private float f;
        private double d;
        private boolean bool;
        private char c;
        private Object on;
        private Object o = new Object();
        private Domain1 domain1 = new Domain1();
        private String str;
        private int[] is;
        private String[] strs;
    }

    private Domain2 d2 = new Domain2();

    @BeforeEach
    public void initData() {
        Domain1 d1 = new Domain1();
        d1.setUsername("name");
        d1.setAge(22);

        d2.setI(10);
        d2.setDomain1(d1);
        int[] is = {2, 4, 6};
        d2.setIs(is);
        String[] strs = {"hello", "world"};
        d2.setStrs(strs);
    }

    @Test
    public void testPopulate() {
        Domain2 target = new Domain2();
        Objects.populate(d2, target);
        assertArrayEquals(d2.getIs(), target.getIs());
    }

    @Test
    public void testPut() {
        Objects.put(d2, "str", "value");
        assertEquals(d2.getStr(), "value");
    }

    @Test
    public void testGet() {
        Object i = Objects.get(d2, "i");
        assertEquals(d2.getI(), i);
    }

    @Data
    private class ContainsArray implements Cloneable {
        private int i = 10;
        private int[] array = {1, 2, 3, 4};
        private volatile int j = 100;

        @Override
        public ContainsArray clone() throws CloneNotSupportedException {
            ContainsArray ca = (ContainsArray) super.clone();
            Objects.put(ca, "array", array.clone());
            return ca;
        }
    }

    @Test
    public void testClone() throws Exception {
        //test deep clone
        ContainsArray original = new ContainsArray();

        ContainsArray clone = original.clone();
        original.getArray()[1] = 20;
        original.setI(100);

        assertNotEquals(original.getI(), clone.getI());
        assertNotEquals(original.getArray()[1], clone.getArray()[1]);
    }

}