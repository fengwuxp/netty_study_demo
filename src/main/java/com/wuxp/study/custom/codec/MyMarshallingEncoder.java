package com.wuxp.study.custom.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;


public class MyMarshallingEncoder {

    //占位字节长度
    private static final int PLACEHOLDER_LENGTH = 4;

    //空白占位，用于预留设置body的数据包长度
    public static final byte[] PLACEHOLDER_BYTES = new byte[PLACEHOLDER_LENGTH];

    private Marshaller marshaller;

    public MyMarshallingEncoder() throws Exception {
        this.marshaller = MarshallingCodeFactory.buildMarshalling();
    }

    public void encode(Object data, ByteBuf out) throws IOException {

        try {
            //获取当前写入的位置，即body开始写的位置
            int beginIndex = out.writerIndex();

            //占位写操作
            out.writeBytes(PLACEHOLDER_BYTES);

            ByteOutput output = new ChannelBufferByteOutput(out);
            this.marshaller.start(output);
            this.marshaller.writeObject(data);
            this.marshaller.finish();

            //获得body内容的长度
            int contentLength =  out.writerIndex() - beginIndex - PLACEHOLDER_LENGTH;
            //将contentLength的值设置到之前的占位字节中
            out.setInt(beginIndex, contentLength);
        } finally {
            marshaller.close();
        }
    }
}
