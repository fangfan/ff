package io.netty.example.common;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by F.Fang on 2015/5/24.
 * Version :2015/5/24
 */
public class CommonServerHandler extends SimpleChannelInboundHandler<byte[]> {

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        if(msg instanceof ByteBuf){
//            System.out.println("hello, server");
//            ByteBuf buf =(ByteBuf)msg;
//            byte[] content = new byte[buf.readableBytes()];
//            buf.readBytes(content);
//            System.out.println("text:" +new String(content));
//        }
//        //ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
//        ctx.writeAndFlush(msg);
//    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
//        System.out.println("hello, server");
//        byte[] content = new byte[msg.readableBytes()];
//        msg.readBytes(content);
        System.out.println("i am server handler!");
        String text = new String(msg);
        System.out.println("text:" + new String(msg));

        if("end".equals(text)){
            ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
        }else {
            ctx.writeAndFlush(msg);
        }


    }
}
