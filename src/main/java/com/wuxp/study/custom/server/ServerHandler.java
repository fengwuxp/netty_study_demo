package com.wuxp.study.custom.server;


import com.wuxp.study.custom.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 当通道被激活的时候
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道被激活");
    }

    /**
     * 当通道里有数据进行读取的时候
     *
     * @param ctx NETTY服务的上下文
     * @param msg 实际读到的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage requestMessage = (NettyMessage) msg;
        System.out.println("server-> " + requestMessage.getBody());

        NettyMessage resp = requestMessage;
        resp.setBody("我是相应数据：" + requestMessage.getHeader().getSessionId());
        resp.getHeader().setSessionId(2L);
        ctx.writeAndFlush(resp);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("数据读取完成!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("---服务端读取数据发生异常---");
        System.out.println(cause.getCause().getMessage());
        ctx.close();
    }
}
