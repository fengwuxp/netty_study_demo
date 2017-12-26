package com.wuxp.study.serial;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
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

        Req req = (Req) msg;
        System.out.println("服务端获取到是数据 : " + req.getId() + " " + req.getName());

        Resp resp = new Resp();
        resp.setId("返回id-> " + req.getId());
        resp.setName("返回name-> " + req.getName());
        resp.setRequestMessage("返回message-> " + req.getRequestMessage());
        resp.setAttachment(req.getAttachment());
        ctx.writeAndFlush(resp);//.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("数据读取完成!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("发生异常");
        System.out.println(cause.getCause().getMessage());
        ctx.close();
    }
}
