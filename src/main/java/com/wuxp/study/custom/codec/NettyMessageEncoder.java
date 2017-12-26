package com.wuxp.study.custom.codec;

import com.wuxp.study.custom.struct.Header;
import com.wuxp.study.custom.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 */
public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    private MyMarshallingEncoder myMarshallingEncoder;

    public NettyMessageEncoder() throws Exception {
        myMarshallingEncoder = new MyMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage message, ByteBuf out) throws Exception {

        System.err.println("开始编码");
        if (message == null || message.getHeader() == null) {
            throw new RuntimeException("编码失败，没有数据信息！");
        }
        Header header = message.getHeader();

        //写入请求头部信息
        out.writeInt(header.getCrcCode());
        out.writeInt(header.getLength());
        out.writeLong(header.getSessionId());
        out.writeByte(header.getType());
        out.writeByte(header.getPriority());

        //写入请求体信息
        Object body = message.getBody();
        if (body != null) {
            //使用MarshallingEncoder进行编码
            this.myMarshallingEncoder.encode(body, out);
        } else {
            //如果没有数据这进行补位，为了方便后续的decoder操作
            out.writeBytes(MyMarshallingEncoder.PLACEHOLDER_BYTES);
        }


        //最后我们要获取整个数据包的长度，也就是header length +body length

        //这里必须要减掉8个字节 LengthFieldBasedFrameDecoder中 lengthFieldOffset + lengthFieldLength
        out.setIndex(4, out.readableBytes() - 8);

        System.out.println("长度-> "+out.readableBytes());
    }
}
