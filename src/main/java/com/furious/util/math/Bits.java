package com.furious.util.math;

public abstract class Bits {

    private Bits() {
    }

    /**
     * i在b位的二进制数
     */
    public static byte bit(byte i, int b) {
        if (b < 0 || b > 8) {
            throw new IllegalArgumentException("b只能在[0,8)之间，传入的b为：" + b);
        }
        byte bit = (byte) (1 << (7 - b));
        return (byte) (i & bit);
    }

    /**
     * i在b位的二进制数
     */
    public static short bit(short i, int b) {
        if (b < 0 || b > 16) {
            throw new IllegalArgumentException("b只能在[0,16)之间，传入的b为：" + b);
        }
        short bit = (short) (1 << (15 - b));
        return (short) (i & bit);
    }

    /**
     * i在b位的二进制数
     */
    public static int bit(int i, int b) {
        if (b < 0 || b > 32) {
            throw new IllegalArgumentException("b只能在[0,32)之间，传入的b为：" + b);
        }
        int bit = 1 << (31 - b);
        return i & bit;
    }

    /**
     * i在b位的二进制数
     */
    public static long bit(long i, int b) {
        if (b < 0 || b > 64) {
            throw new IllegalArgumentException("b只能在[0,64)之间，传入的b为：" + b);
        }
        long bit = (long) 1 << (63 - b);
        return i & bit;
    }

    /**
     * i的二进制形式中1的个数
     */
    public static int bitCount(byte i) {
        return bitCount(i << 24);
    }

    /**
     * i的二进制形式中1的个数
     */
    public static int bitCount(short i) {
        return bitCount(i << 16);
    }

    /**
     * i的二进制形式中1的个数
     */
    public static int bitCount(int i) {
//        int bits = 0;
//        while (i != 0) {
//            if (1 == (1 & i)) {
//                bits++;
//            }
//            i >>>= 1;
//        }
//        return bits;
        return Integer.bitCount(i);
    }

    /**
     * i的二进制形式中1的个数
     */
    public static int bitCount(long i) {
//        int bits = 0;
//        while (i != 0) {
//            if (1 == ((long) 1 & i)) {
//                bits++;
//            }
//            i >>>= 1;
//        }
//        return bits;
        return Long.bitCount(i);
    }

    /**
     * i的二进制表示形式（最高位补齐0）
     */
    public static String fullBinaryLayout(int i) {
        StringBuilder layout = new StringBuilder();
        int bit = 1 << 31;
        while (bit != 0) {
            if ((i & bit) != 0) {
                layout.append("1");
            } else {
                layout.append("0");
            }
            bit >>>= 1;
        }
        return layout.toString();
    }

    /**
     * i的二进制表现形式中，1是否连续
     */
    public static boolean oneBitsIsConsecutive(int i) {
        int bitCount = bitCount(i);
        if (bitCount <= 1) {
            return false;
        }
        return 32 - bitCount == Integer.numberOfLeadingZeros(i) + Integer.numberOfTrailingZeros(i);
    }

}
