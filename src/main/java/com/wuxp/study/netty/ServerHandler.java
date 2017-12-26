package com.wuxp.study.netty;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.ByteBuffer;

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

        //   try {
        ByteBuf buffer = (ByteBuf) msg;
        byte[] request = new byte[buffer.readableBytes()];
        buffer.readBytes(request);
        String body = new String(request, "UTF-8");
        System.out.println("服务端接收的到的数据-> " + body);

        //写回数据
        String resp = "返回数据" + System.currentTimeMillis();
       // Thread.sleep(6000);
        ctx.writeAndFlush(Unpooled.copiedBuffer(resp.getBytes("UTF-8")));
                //添加addListener 可以触发通道监听事件
              //  .addListener(ChannelFutureListener.CLOSE);
//        } finally {
//            //ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放。请记住处理器的职责是释放所有传递到处理器的引用计数对象
//           // buffer.release();
//            ReferenceCountUtil.release(msg);
//        }
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
