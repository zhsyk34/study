package com.cat.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by Archimedes on 2016/07/24.
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncServerHandle> {

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncServerHandle attachment) {
        attachment.server.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncServerHandle attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
