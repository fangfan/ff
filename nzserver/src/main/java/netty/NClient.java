package netty;

import exception.CommonException;
import exception.ServerInitException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by F.Fang on 2015/5/25.
 * Version :2015/5/25
 */
public class NClient {
    private static final Logger LOG = LoggerFactory.getLogger(NServer.class);

    private EventLoopGroup group;
    private Channel channel;

    public static NClient getInstance(String ip, int port) {
        return new NClient(ip, port);
    }

    private NClient(String ip, int port) {
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
                                    .addLast(new ClientMsgHandler());
                        }
                    }).option(ChannelOption.TCP_NODELAY, true);
            // Make the connection attempt.
            ChannelFuture f = b.connect(ip, port).sync();
            this.channel = f.channel();
            // Wait until the connection is closed.
        } catch (Exception e) {
            LOG.error("init netty client occur exception!", e);
            throw new ServerInitException("init netty client occur exception!", e);
        }
    }

    public void sendMsg(byte[] msg) throws CommonException {
        if (msg != null && msg.length > 0) {
            ByteBuf buf = Unpooled.copiedBuffer(msg);
            try {
                channel.writeAndFlush(buf).sync();
            } catch (InterruptedException e) {
                LOG.error("client send msg occur exception!", e);
                throw new CommonException("client send msg occur exception!", e);
            }
        }
    }

    public void shutdown() {
        if (channel != null) {
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                LOG.error("close channel occur exception!", e);
            } finally {
                if (group != null) {
                    group.shutdownGracefully();
                }
            }
        }
    }
}
