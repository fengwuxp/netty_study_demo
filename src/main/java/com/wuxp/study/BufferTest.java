package com.wuxp.study;

import java.nio.Buffer;
import java.nio.IntBuffer;

public class BufferTest {

    public static void main(String[] args) {
        IntBuffer intBuffer= IntBuffer.allocate(10);
        intBuffer.put(12);
        intBuffer.put(2);
        intBuffer.put(3);
        intBuffer.put(5);
        IntBuffer duplicate = intBuffer.duplicate();
        duplicate.put(77);
        System.out.println(intBuffer.limit(6));
        System.out.println(intBuffer.get(4));
        intBuffer.flip();
        System.out.println(intBuffer.get());

    }
}
