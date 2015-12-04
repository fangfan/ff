package io.netty.example.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * Created by F.Fang on 2015/5/26.
 * Version :2015/5/26
 */
public class CommonServer1 {


    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    private CommonServer1(int port) {
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ByteArrayDecoder())
                                    .addLast(new ByteArrayEncoder())
                                    .addLast(new CommonServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            serverChannel = b.bind(port).sync().channel();
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (serverChannel != null) {
            // shut down your server.
            ChannelFuture chFuture = serverChannel.close();
            chFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    shutdownWorkers();
                }
            });
        } else {
            shutdownWorkers();
        }
    }

    private void shutdownWorkers() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        CommonServer1 server = new CommonServer1(8080);
        Thread.sleep(10000);
        server.stop();
    }
}
