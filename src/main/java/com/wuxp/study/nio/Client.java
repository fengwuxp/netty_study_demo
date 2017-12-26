package com.wuxp.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Client {


    private static ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    private static ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws Exception {

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9000);

        //打开客户端通道
        SocketChannel socketChannel = SocketChannel.open();

        //与服务端建了连接
        socketChannel.connect(inetSocketAddress);

        //设置为非阻塞式的
        socketChannel.configureBlocking(false);

      final   Selector selector = Selector.open();

        socketChannel.register(selector, SelectionKey.OP_WRITE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        //开启多路复用器监听
                        int select = selector.select();


                        //获取多路复用器注册的通道key
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

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
                                accept(selectionKey, selector);
                            }
                            if (selectionKey.isWritable()) {
                                //可写状态处理
                                writ(selectionKey, selector);
                            }

                            if (selectionKey.isReadable()) {
                                //可读状态处理
                                read(selectionKey, selector);
                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


//        while (true) {
//
//
//            byte[] bytes = new byte[1024];
//
//            System.in.read(bytes);
//            byteBuffer.put(bytes);
//
//            //复位
//            byteBuffer.flip();
//
//            //发送到服务端
//            socketChannel.write(byteBuffer);
//
//            //清空缓存区
//            byteBuffer.clear();
//        }
    }

    /**
     * 监听阻塞状态方法处理
     *
     * @param selectionKey
     */
    private static void accept(SelectionKey selectionKey, Selector selector) {
        try {
            //此处取到的一定是客户端的 SocketChannel
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

            //设置客户端的通道为非阻塞
            socketChannel.configureBlocking(false);

            //注册到多路复用器上
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static int i = 0;

    private static void writ(SelectionKey selectionKey, Selector selector) {

        System.out.println("向服务端端提交数据");

        //客户端的 SocketChannel
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        try {
            String text = "了逻辑--> " + (i++);
            writeBuffer.put(text.getBytes("UTF-8"));
            writeBuffer.flip();
            channel.write(writeBuffer);
            //清空缓冲区
            writeBuffer.clear();

            //注册到多路复用器上,连接
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void read(SelectionKey selectionKey, Selector selector) {

        readBuffer.clear();
        //客户端的 SocketChannel
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        try {
            //将数据读入缓冲区
            int read = channel.read(readBuffer);
            if (read == -1) {
                selectionKey.channel().close();
                selectionKey.cancel();
            }

            //读取buffer中的数据前，要先对buffer进行复位
            readBuffer.flip();

            byte[] bytes = new byte[readBuffer.remaining()];
            readBuffer.get(bytes);

            System.out.println("客户端端获取到的数据-> " + new String(bytes, "UTF-8"));

            //注册到多路复用器上,可写的
            channel.register(selector, SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
