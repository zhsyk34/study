package com.cat.netty3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Archimedes on 2016-07-27.
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String response = (String) msg;
        System.out.println(++counter + " [" + response + "]");

        ByteBuf buf = Unpooled.copiedBuffer("Hello client.$$".getBytes());
        ctx.writeAndFlush(buf);
    }
}
