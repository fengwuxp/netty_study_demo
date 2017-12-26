package com.wuxp.study.custom.client;

import com.wuxp.study.custom.codec.NettyMessageDecoder;
import com.wuxp.study.custom.codec.NettyMessageEncoder;
import com.wuxp.study.custom.struct.Header;
import com.wuxp.study.custom.struct.NettyMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
                        pipeline.addLast(new NettyMessageDecoder(1024 * 1024 * 5, 4, 4));
                        pipeline.addLast(new NettyMessageEncoder());
                        pipeline.addLast(new ClientHandler());
                    }
                });


        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).syncUninterruptibly();

        System.out.println("client start...");

        Channel channel = channelFuture.channel();
        for (int i = 0; i < 100; i++) {
            NettyMessage nettyMessage = new NettyMessage();
            Header header = new Header();
            header.setType(Byte.valueOf("0"));
            header.setPriority(Byte.valueOf("2"));
            header.setSessionId(System.currentTimeMillis());
            nettyMessage.setHeader(header);
            nettyMessage.setBody("我是请求的数据" + i);
            channel.writeAndFlush(nettyMessage);
        }

        //   channelFuture.channel().writeAndFlush("测试");

        long l = System.currentTimeMillis();
        //释放连接
        channel.closeFuture().sync();
        worker.shutdownGracefully();
        System.out.println((System.currentTimeMillis() - l) / 1000);
    }
}
