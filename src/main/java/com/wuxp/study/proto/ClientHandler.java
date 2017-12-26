package com.wuxp.study.proto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        for (int i = 0; i < 10; i++) {
            ctx.write(build(i));
        }
        ctx.flush();
    }

    private RequestDTO.Request build(int i) {
        RequestDTO.Request.Builder builder = RequestDTO.Request.newBuilder();
        return builder.setEmail("110818213@qq.com")
                .setId(i)
                .setName("张三" + i)
                .build();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ResponeDTO.Respone respone = (ResponeDTO.Respone) msg;

            System.out.println("服务端响应数据-->" + respone.getName());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
