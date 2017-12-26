package com.wuxp.study.serial;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class Server {

    public static void main(String[] args) {

        //用于接收客户端连接的线程工作组
        EventLoopGroup boss = new NioEventLoopGroup();

        //用于处理客户端连接的工作组
        EventLoopGroup worker = new NioEventLoopGroup();

        //是一个启动NIO服务的辅助启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)  //绑定2个工作线程组
                .channel(NioServerSocketChannel.class)  //设置NIO模式
                .option(ChannelOption.SO_BACKLOG, 1024)  //设置TCP连接数缓存区大小
                .option(ChannelOption.SO_RCVBUF, 32 * 1024) //设置接收数据的缓冲区大小
                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024) //设置数据的缓冲区大小
                .childOption(ChannelOption.SO_KEEPALIVE, true) //设置长连接
                //初始化绑定服务通道
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        //为通道进行初始化，数据传输过拉里的时候会进行拦截和执行
                        channel.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                        channel.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());

                        channel.pipeline().addLast(new ServerHandler());
                    }
                });

        try {
            //绑定端口，启动服务，并使用sync(同步)方法阻塞
            ChannelFuture channelFuture = bootstrap.bind(9000).sync();
            //释放连接
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
