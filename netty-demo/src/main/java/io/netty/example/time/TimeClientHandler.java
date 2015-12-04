package io.netty.example.time;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by F.Fang on 2015/5/22.
 * Version :2015/5/22
 */
public class TimeClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // Server is supposed to send nothing, but if it sends something, discard it.
        System.out.println("client receive msg:" + msg);
    }

}
