package io.netty.example.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by F.Fang on 2015/5/22.
 * Version :2015/5/22
 */
public class TimeClient {

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
                            p.addLast(new StringEncoder())
                            .addLast(new StringDecoder())
                            .addLast(new TimeClientHandler());
                        }
                    }).option(ChannelOption.TCP_NODELAY, true);

            // Make the connection attempt.
            ChannelFuture f = b.connect("192.168.24.36", 8080).sync();
            Channel channel = f.channel();
            channel.writeAndFlush("hello world!").sync();
            channel.closeFuture().sync();
            // Wait until the connection is closed.

        } finally {
            group.shutdownGracefully();
        }
    }
}
