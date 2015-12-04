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
public class CommonClient {
    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
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
            Channel channel = f.channel();
            ByteBuf buf = Unpooled.copiedBuffer("hello world!".getBytes());
            channel.writeAndFlush(buf).sync();

            Thread.sleep(10000);

            ByteBuf buf1 = Unpooled.copiedBuffer("end".getBytes());
            channel.writeAndFlush(buf1).sync();

            channel.closeFuture().sync();
            // Wait until the connection is closed.

        } finally {
            group.shutdownGracefully();
        }
    }
}
