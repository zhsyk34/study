@param   <V>     The result type of the I/O operation
@param   <A>     The type of the object attached to the I/O operation
CompletionHandler<V,A>

AsynchronousServerSocketChannel
public abstract <A> void accept(A attachment, CompletionHandler<AsynchronousSocketChannel,? super A> handler);

AsynchronousSocketChannel
public abstract <A> void connect(SocketAddress remote,A attachment,CompletionHandler<Void,? super A> handler);
public final <A> void read(ByteBuffer dst,A attachment,CompletionHandler<Integer,? super A> handler)
public final <A> void write(ByteBuffer src,A attachment,CompletionHandler<Integer,? super A> handler)