package netty;

import exception.ServerInitException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by F.Fang on 2015/5/25.
 * Version :2015/5/25
 */
public class NServer {
    private static final Logger LOG = LoggerFactory.getLogger(NServer.class);

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public static NServer getInstance(int port) {
        return new NServer(port);
    }

    private NServer(int port) {
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
                                    .addLast(new ServerMsgHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            serverChannel = b.bind(port).sync().channel();
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully

        } catch (Exception e) {
            LOG.error("init netty server occur exception!", e);
            throw new ServerInitException("init netty server occur exception!", e);
        }
    }

    public void shutdown() {
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
        NServer server = NServer.getInstance(8090);
        Thread.sleep(5000);
        server.shutdown();
    }

}
