package com.wuxp.study.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Client {

    public static void main(String[] args) throws Exception {

        EventLoopGroup worker = new NioEventLoopGroup();

        //是一个启动NIO服务的辅助启动类
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)  //绑定1个工作线程组
                .channel(NioSocketChannel.class)  //设置NIO模式
                //初始化绑定服务通道
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        //为通道进行初始化，数据传输过拉里的时候会进行拦截和执行
                        ChannelPipeline pipeline = channel.pipeline();
//                        pipeline.addLast(new StringEncoder());
//                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new ReadTimeoutHandler(6));
                        pipeline.addLast(new ClientHandler());
                    }
                });


        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).syncUninterruptibly();
        channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer("这是客户端发送数据".getBytes("UTF-8")));
        //   channelFuture.channel().writeAndFlush("测试");

        long l = System.currentTimeMillis();
        //释放连接
        channelFuture.channel().closeFuture().sync();
        worker.shutdownGracefully();
        System.out.println((System.currentTimeMillis()-l)/1000);
    }
}
