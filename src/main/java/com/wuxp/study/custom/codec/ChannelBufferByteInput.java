package com.wuxp.study.custom.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;

import java.io.IOException;

public class ChannelBufferByteInput implements ByteInput {

    private ByteBuf byteBuf;

    public ChannelBufferByteInput(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public int read() throws IOException {
        return this.byteBuf.readableBytes();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return this.read(b,off,len);
    }

    @Override
    public int available() throws IOException {
        return 0;
    }

    @Override
    public long skip(long n) throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
