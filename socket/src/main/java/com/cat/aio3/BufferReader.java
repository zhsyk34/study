package com.cat.aio3;

import com.cat.nio.base.Util;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public abstract class BufferReader implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;

    private boolean doAfter;

    public BufferReader(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public BufferReader(AsynchronousSocketChannel channel, boolean doAfter) {
        this.channel = channel;
        this.doAfter = doAfter;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        String read = new String(bytes);
        System.out.println("read data: " + read);

        if (doAfter) {
            doAfter(channel);
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        Util.close(channel);
    }

    protected abstract void doAfter(AsynchronousSocketChannel channel);

}
