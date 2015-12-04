package netty;

import common.ByteUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by F.Fang on 2015/5/25.
 * Version :2015/5/25
 */
public class ServerMsgHandler extends SimpleChannelInboundHandler<byte[]> {
    private static final Logger LOG = LoggerFactory.getLogger(ServerMsgHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
        try {
            byte msgType = bytes[0];
            byte[] msgSizeBytes = new byte[2];
            msgSizeBytes[0] = bytes[1];
            msgSizeBytes[1] = bytes[2];
            int size = ByteUtil.byte2ToShort(msgSizeBytes);
            byte[] content = Arrays.copyOfRange(bytes, 3, (size + 3));
            String text = new String(content);
            LOG.info("server received msg:" +text);
            ctx.writeAndFlush("success".getBytes()).addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            ctx.writeAndFlush("fail".getBytes()).addListener(ChannelFutureListener.CLOSE);
        }
    }

}
