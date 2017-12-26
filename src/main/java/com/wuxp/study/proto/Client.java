package com.wuxp.study.proto;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

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

                        //设置数据识别
                        channel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        channel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());

                        //设置编解码
                        channel.pipeline().addLast(new ProtobufDecoder(ResponeDTO.Respone.getDefaultInstance()));
                        channel.pipeline().addLast(new ProtobufEncoder());

                        channel.pipeline().addLast(new ClientHandler());
                    }
                });


        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).syncUninterruptibly();
    //    channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer("这是客户端发送数据".getBytes("UTF-8")));
        //   channelFuture.channel().writeAndFlush("测试");

        //释放连接
        channelFuture.channel().closeFuture().sync();
        worker.shutdownGracefully();
    }
}
