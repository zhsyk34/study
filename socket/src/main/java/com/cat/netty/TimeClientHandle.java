package com.cat.netty;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Archimedes on 2016/07/25.
 */
public class TimeClientHandle extends ChannelHandlerAdapter {

    private final ByteBuf first;

    public TimeClientHandle() {
        String message = "Hello server,I am client : " + new Random().nextInt(53);
        byte[] bytes = message.getBytes();
        first = Unpooled.buffer(bytes.length);
        first.writeBytes(bytes);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(first);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        System.out.println("server tell me now is : " + new String(req, "UTF-8"));
    }
}
