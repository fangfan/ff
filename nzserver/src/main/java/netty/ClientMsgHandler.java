package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by F.Fang on 2015/5/25.
 * Version :2015/5/25
 */
public class ClientMsgHandler extends SimpleChannelInboundHandler<byte[]> {
    private static final Logger LOG = LoggerFactory.getLogger(ClientMsgHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        LOG.info("client received msg:" +new String(msg));
    }
}
