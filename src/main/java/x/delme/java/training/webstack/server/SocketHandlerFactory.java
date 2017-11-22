package x.delme.java.training.webstack.server;

import java.net.Socket;

public interface SocketHandlerFactory {

	public SocketHandler createSocketHandler(Socket socket);
}
