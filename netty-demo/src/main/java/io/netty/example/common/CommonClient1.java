package io.netty.example.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * Created by F.Fang on 2015/5/24.
 * Version :2015/5/24
 */
public class CommonClient1 {

    private EventLoopGroup group;

    private Channel channel;

    public CommonClient1() {
        group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ByteArrayEncoder())
                                    .addLast(new ByteArrayDecoder())
                                    .addLast(new CommonClientHandler());
                        }
                    }).option(ChannelOption.TCP_NODELAY, true);

            // Make the connection attempt.
            ChannelFuture f = b.connect("127.0.0.1", 8080).sync();
            this.channel = f.channel();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(){
        if(channel!=null){
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                if(group!=null){
                    group.shutdownGracefully();
                }
            }
        }
    }


    public void sendMsg(String msg) throws Exception {
        if (msg != null && msg.length() > 0) {
            ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes());
            channel.writeAndFlush(buf).sync();
        }
    }

    public static void main(String[] args) throws Exception {
        CommonClient1 client = new CommonClient1();
        client.sendMsg("hello world!");
        Thread.sleep(3000);
        client.sendMsg("end");
        client.close();
    }
}
