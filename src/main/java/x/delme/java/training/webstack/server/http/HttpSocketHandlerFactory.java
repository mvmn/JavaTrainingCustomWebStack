package x.delme.java.training.webstack.server.http;

import java.net.Socket;

import x.delme.java.training.webstack.server.SocketHandler;
import x.delme.java.training.webstack.server.SocketHandlerFactory;

public class HttpSocketHandlerFactory implements SocketHandlerFactory {

	public HttpSocketHandlerFactory(String httpRoutesConfigString) {

	}

	@Override
	public SocketHandler createSocketHandler(Socket socket) {
		return new HttpSocketHandler(socket);
	}
}
