package com.wuxp.study.aio;


import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    //线程池
    private ExecutorService executorService;

    //线程组
    private AsynchronousChannelGroup threadGroup;

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public Server(int port) {

        //创建一个缓存池
        executorService= Executors.newCachedThreadPool();

        try {
            //创建线程组
            threadGroup=AsynchronousChannelGroup.withCachedThreadPool(executorService,1);

            //创建服务器通道
            asynchronousServerSocketChannel=AsynchronousServerSocketChannel.open(threadGroup);

            //绑定端口
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));

            System.out.println("server start，port-> "+port);

            //进行阻塞
            asynchronousServerSocketChannel.accept(this,new ServerCompletionHandler());

            //一直阻塞，不让服务器停止
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public AsynchronousServerSocketChannel getAsynchronousServerSocketChannel() {
        return asynchronousServerSocketChannel;
    }

    public static void main(String[] args) {
        Server server = new Server(9000);
    }
}
