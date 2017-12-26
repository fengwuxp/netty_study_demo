package com.wuxp.study.aio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Server attachment) {

        //当有下一个客户端接入的时候，直接调用Server的accept方法,这样反复执行，保证多个客户端都可以阻塞
        attachment.getAsynchronousServerSocketChannel().accept(attachment, this);
        read(socketChannel);
    }

    @Override
    public void failed(Throwable exc, Server attachment) {
        exc.printStackTrace();
    }

    private void read(final AsynchronousSocketChannel socketChannel) {

        //读取数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer resultSize, ByteBuffer attachment) {
                //进行读取之后，重置标识位
                attachment.flip();

                //获取读取的字节数
                System.out.println("Server--> 接受到的的客户端数据长度为：" + resultSize);

                //获取读取的数据
                String result = "";
                try {
                    result = new String(attachment.array(), "UTF-8").trim();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("Server--> 接受到的的客户端数据为：" + result);
                String response = "服务端响应，收到了了客户端发来的数据：" + result;
                write(socketChannel, response);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });

    }

    private void write(AsynchronousSocketChannel socketChannel, String respDate) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            byteBuffer.put(respDate.getBytes("UTF-8"));
            byteBuffer.flip();
            Integer integer = socketChannel.write(byteBuffer).get();
            System.out.println("服务端写回长度-> " + integer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
