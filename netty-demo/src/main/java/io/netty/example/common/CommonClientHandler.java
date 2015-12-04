package io.netty.example.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by F.Fang on 2015/5/24.
 * Version :2015/5/24
 */
class CommonClientHandler extends SimpleChannelInboundHandler<byte[]> {

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        if (msg instanceof ByteBuf) {
//            System.out.println("hello, client");
//            ByteBuf buf = (ByteBuf) msg;
//            byte[] content = new byte[buf.readableBytes()];
//            buf.readBytes(content);
//            System.out.println("text:" + new String(content));
//        }
//        System.out.println("msg: "+msg);
//        ctx.write(msg);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        System.out.println("i am client handler!");
        System.out.println("text:" + new String(msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
