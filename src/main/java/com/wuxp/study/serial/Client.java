package com.wuxp.study.serial;

import com.wuxp.study.utils.GzipUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
                        //序列化
                        channel.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                        channel.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                        channel.pipeline().addLast(new ClientHandler());
                    }
                });


        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync();

        String readPath = System.getProperty("user.dir") + File.separatorChar
                + "sources" + File.separatorChar + "005.jpg";

        for (int i = 0; i < 100; i++) {
            Req req = new Req();
            req.setId(i + "");
            req.setName("姓名-> " + i);
            req.setRequestMessage("数据信息-> " + i);

            File file = new File(readPath);
            InputStream in = new FileInputStream(file);
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            req.setAttachment(GzipUtils.gzip(data));
            //传输数据
            channelFuture.channel().writeAndFlush(req);
        }

        //释放连接
        channelFuture.channel().closeFuture().sync();
        worker.shutdownGracefully();
    }
}
