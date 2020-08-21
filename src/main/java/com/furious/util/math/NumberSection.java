package com.furious.util.math;

import java.util.Objects;

import lombok.Data;

/**
 * [start,end]
 * 数值区间
 * 可用于判断两个数值区间和两个时间段是否有交集
 */
@Data
public class NumberSection {

    private long start;
    private long end;

    /**
     * 判断和另外一个数值区间是否有交集
     */
    public boolean intersect(NumberSection another) {
        Objects.requireNonNull(another);
        long s = another.getStart();
        long e = another.getEnd();
        if (start > end || s > e) {
            throw new IllegalArgumentException("请确保start不大于end");
        }
        if (start == s) {
            return true;
        }

        long min = Math.min(start, s);
        long s1 = start - min;
        long e1 = end - min;
        long s2 = s - min;
        long e2 = e - min;

        if (min == start) {
            return s2 <= e1;
        }

        //min == s
        return s1 <= e2;
    }
}
