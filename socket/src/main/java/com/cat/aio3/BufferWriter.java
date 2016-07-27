package com.cat.aio3;

import com.cat.nio.base.Util;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public abstract class BufferWriter implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;

    private boolean doAfter;

    public BufferWriter(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public BufferWriter(AsynchronousSocketChannel channel, boolean doAfter) {
        this.channel = channel;
        this.doAfter = doAfter;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            System.out.println("continue write data...");
            channel.write(buffer, buffer, this);
        } else if (doAfter) {
            doAfter(channel);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        exc.printStackTrace();
        Util.close(channel);
    }

    protected abstract void doAfter(AsynchronousSocketChannel channel);

}
