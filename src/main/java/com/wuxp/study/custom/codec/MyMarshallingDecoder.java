package com.wuxp.study.custom.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Unmarshaller;


public class MyMarshallingDecoder {

    private Unmarshaller unmarshaller;

    public MyMarshallingDecoder() throws Exception {
        this.unmarshaller = MarshallingCodeFactory.buildUnMarshalling();
    }

    public Object decode(ByteBuf in) throws Exception {

        try {
            //1：首先读取4个长度（实际body内容的长度）
            int bodySize = in.readInt();

            //2：读取实际body的缓冲内容
            ByteBuf byteBuf = in.slice(in.readerIndex(), bodySize);

            //3：转化
            ChannelBufferByteInput input = new ChannelBufferByteInput(byteBuf);

            //4：读取操作
            this.unmarshaller.start(input);
            Object result = this.unmarshaller.readObject();
            this.unmarshaller.finish();
            //读取完毕后，更新当前读取起始位置
            //这里更新的原因是 input中传入的用slice方法截取出来的ByteBuf对象
            //这样是并没有改变in中的readerIndex，所以这里需要更新
            in.readerIndex(in.readerIndex() + bodySize);
            // in.resetReaderIndex();
            return result;
        } finally {
            this.unmarshaller.close();
        }

    }
}
