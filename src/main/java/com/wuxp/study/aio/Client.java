package com.wuxp.study.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class Client implements Runnable{

    private AsynchronousSocketChannel asynchronousSocketChannel;

    public Client() throws Exception {

        asynchronousSocketChannel = AsynchronousSocketChannel.open();
    }

    public void connect() {
        asynchronousSocketChannel.connect(new InetSocketAddress("127.0.0.1", 9000));
    }

    public void write(String request) throws Exception {
        asynchronousSocketChannel.write(ByteBuffer.wrap(request.getBytes("UTF-8"))).get();
        read();
    }

    private void read() throws Exception{
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        asynchronousSocketChannel.read(byteBuffer).get();
        byteBuffer.flip();
        byte[] respBytes=new byte[byteBuffer.remaining()];
        byteBuffer.get(respBytes);

        System.out.println(new String(respBytes,"UTF-8").trim());
    }

    @Override
    public void run() {
        while (true){

        }
    }

    public static void main(String[] args) throws Exception{
        Client c1=new Client();
        c1.connect();

        Client c2=new Client();
        c2.connect();

        Client c3=new Client();
        c3.connect();

        new Thread(c1,"c1").start();
        new Thread(c2,"c2").start();
        new Thread(c3,"c3").start();

        Thread.sleep(1000);
        c1.write("c1  111");
        c2.write("c2  222");
        c3.write("c3  333");
    }
}
