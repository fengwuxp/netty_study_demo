package com.wuxp.study.custom.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

public class ChannelBufferByteOutput implements ByteOutput {

    private ByteBuf byteBuf;


    public ChannelBufferByteOutput(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public void write(int b) throws IOException {
        this.byteBuf.writeByte(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.byteBuf.writeBytes(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.byteBuf.writeBytes(b,off,len);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }
}
