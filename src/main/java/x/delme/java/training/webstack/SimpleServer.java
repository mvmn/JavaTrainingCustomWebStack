package x.delme.java.training.webstack;

import java.io.IOException;
import java.net.ServerSocket;

import x.delme.java.training.webstack.server.SocketHandlerFactory;

public class SimpleServer {

	protected final int port;
	protected final SocketHandlerFactory handlerFactory;
	protected final Object LOCK = new Object();
	protected volatile ServerSocket serverSocket;

	public SimpleServer(int port, SocketHandlerFactory handlerFactory) {
		this.port = port;
		this.handlerFactory = handlerFactory;
	}

	public void start() throws IOException {
		synchronized (LOCK) {
			if (this.serverSocket == null) {
				this.serverSocket = new ServerSocket(port);
				System.out.println("Listening at port " + port + "...");
				while (!this.serverSocket.isClosed()) {
					try {
						new Thread(handlerFactory.createSocketHandler(serverSocket.accept())).start();
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
				System.out.println("Finished listening at port " + port + ".");
			}
		}
	}

	public void stop() throws IOException {
		synchronized (LOCK) {
			if (this.serverSocket != null) {
				this.serverSocket.close();
				this.serverSocket = null;
			}
		}
	}
}
