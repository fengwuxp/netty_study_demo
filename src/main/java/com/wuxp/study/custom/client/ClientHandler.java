package com.wuxp.study.custom.client;

import com.wuxp.study.custom.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            NettyMessage nettyMessage = (NettyMessage) msg;
            System.out.println("client-> " + nettyMessage.getBody());

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("---客户端读取数据发生异常---");
        System.out.println(cause.getCause().getMessage());
        ctx.close();
    }
}
