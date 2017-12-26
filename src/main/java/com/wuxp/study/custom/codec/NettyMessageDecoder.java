package com.wuxp.study.custom.codec;

import com.wuxp.study.custom.struct.Header;
import com.wuxp.study.custom.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * LengthFieldBasedFrameDecoder 是为了解决的拆包粘包等问题
 * 解码器
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    private MyMarshallingDecoder myMarshallingDecoder;


    /**
     * @param maxFrameLength    最大的帧长度
     * @param lengthFieldOffset 长度属性的偏移量
     * @param lengthFieldLength 代表长度属性的长度
     */
    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws Exception {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.myMarshallingDecoder = new MyMarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decode = super.decode(ctx, in);
        if (decode == null) {
            return null;
        }
        ByteBuf frame = (ByteBuf) decode;

        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();

        //TODO crcCOde 通信标记已验证
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionId(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        nettyMessage.setHeader(header);

        if (frame.readableBytes() > 4) {
            nettyMessage.setBody(myMarshallingDecoder.decode(frame));
        }
        return nettyMessage;
    }
}
