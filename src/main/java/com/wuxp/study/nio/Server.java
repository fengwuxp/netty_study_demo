package com.wuxp.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {

    private ByteBuffer readBuffer;

    private ByteBuffer writeBuffer;

    private Selector selector;

    public Server(int port) {
        this.readBuffer = ByteBuffer.allocate(1024);
        this.writeBuffer = ByteBuffer.allocate(1024);
        try {
            //打开多路复用器
            this.selector = Selector.open();

            //打开服务器端的通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            //设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);

            //绑定地址
            serverSocketChannel.bind(new InetSocketAddress(port));

            //把服务器通道注册到多路复用器上，并且监听阻塞
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(2);
                //开启多路复用器监听
                int select = this.selector.select();

                //获取多路复用器注册的通道key
                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

                //遍历获取key
                while (iterator.hasNext()) {

                    //获取key
                    SelectionKey selectionKey = iterator.next();

                    //移除 key
                    iterator.remove();

                    //验证通道的有效期
                    if (!selectionKey.isValid()) {
                        continue;
                    }

                    //对SelectionKey的状态做判断
                    if (selectionKey.isAcceptable()) {
                        //阻塞状态处理
                        this.accept(selectionKey);
                    }
                    if (selectionKey.isReadable()) {
                        //可读状态处理
                        this.read(selectionKey);
                    }
                    if (selectionKey.isWritable()) {
                        //可写状态处理
                        this.writ(selectionKey);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 监听阻塞状态方法处理
     *
     * @param selectionKey
     */
    private void accept(SelectionKey selectionKey) {
        try {
            //此处取到的一定是服务端的 ServerSocketChannel
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();

            //获取到客户端的SocketChannel
            SocketChannel socketChannel = serverSocketChannel.accept();

            //设置客户端的通道为非阻塞
            socketChannel.configureBlocking(false);

            //注册到多路复用器上
            socketChannel.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void read(SelectionKey selectionKey) {
        //清空缓冲区
        this.readBuffer.clear();

        //客户端的 SocketChannel
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        try {
            //将数据读入缓冲区
            int read = channel.read(this.readBuffer);
            if (read == -1) {
                selectionKey.channel().close();
                selectionKey.cancel();
            }

            //读取buffer中的数据前，要先对buffer进行复位
            this.readBuffer.flip();

            byte[] bytes = new byte[this.readBuffer.remaining()];
            this.readBuffer.get(bytes);

            System.out.println("服务端获取到的数据-> " + new String(bytes, "UTF-8"));

            //注册到多路复用器上,可写的
            channel.register(this.selector, SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writ(SelectionKey selectionKey) {

        System.out.println("向客户端写回数据");

        //客户端的 SocketChannel
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        try {
            Date date = new Date();
            this.writeBuffer.put(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date).getBytes("UTF-8"));
            this.writeBuffer.clear();
            channel.write(this.writeBuffer);
            //清空缓冲区
            this.writeBuffer.clear();

            //注册到多路复用器上,连接
            channel.register(this.selector, SelectionKey.OP_READ);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Server server=new Server(9000);
        Thread thread=new Thread(server);
        thread.start();
    }
}
