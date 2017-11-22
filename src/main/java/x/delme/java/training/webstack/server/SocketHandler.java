package x.delme.java.training.webstack.server;

import java.net.Socket;

public abstract class SocketHandler implements Runnable {

	protected final Socket socket;

	public SocketHandler(Socket socket) {
		this.socket = socket;
	}
}
