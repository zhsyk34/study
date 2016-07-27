package com.cat.nio.base;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public abstract class SelectHandle {

    private Selector selector;

    private int timeout;

    private volatile boolean stop;

    public SelectHandle(Selector selector, int timeout) {
        this.selector = selector;
        this.timeout = timeout;
    }

    public void stop() {
        this.stop = true;
    }

    public final void handle() throws Throwable {
        while (!stop) {
            selector.select(timeout);

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            SelectionKey key;
            while (it.hasNext()) {
                key = it.next();
                it.remove();
                try {
                    this.KeyHandle(key);
                } catch (IOException e) {
                    if (key != null) {
                        key.cancel();
                        Util.close(key.channel());
                    }
                }
            }
        }
        Util.close(selector);
    }

    protected final void KeyHandle(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return;
        }

        if (key.isAcceptable()) {
            accept(key);
        }
        if (key.isConnectable()) {
            connect(key);
        }
        if (key.isReadable()) {
            read(key);
        }
        if (key.isWritable()) {
            write(key);
        }
    }

    ;

    protected abstract void accept(SelectionKey key) throws IOException;

    protected abstract void connect(SelectionKey key) throws IOException;

    protected abstract void read(SelectionKey key) throws IOException;

    protected abstract void write(SelectionKey key) throws IOException;

}
