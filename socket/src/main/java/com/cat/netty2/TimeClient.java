package com.cat.netty2;

import com.cat.config.Config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Archimedes on 2016/07/25.
 */
public class TimeClient {

    public static void main(String[] args) throws Exception {
        new TimeClient().connect(Config.HOST, Config.PORT);
//		System.out.print("line:" + System.getProperty("line.separator"));
    }

    public void connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new TimeClientHandle());
                    }
                });
        System.out.println("client connect...");
        ChannelFuture f = bootstrap.connect(host, port).sync();
        f.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
