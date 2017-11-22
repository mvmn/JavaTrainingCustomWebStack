package x.delme.java.training.webstack.server.http;

import java.net.Socket;

import x.delme.java.training.webstack.server.SocketHandler;
import x.delme.java.training.webstack.server.SocketHandlerFactory;
import x.delme.java.training.webstack.server.http.config.HttpResponsesConfig;

public class HttpSocketHandlerFactory implements SocketHandlerFactory {

	protected final HttpResponsesConfig responsesConfig;

	public HttpSocketHandlerFactory(String httpRoutesConfigString) {
		this.responsesConfig = HttpResponsesConfig.parseConfig(httpRoutesConfigString);
	}

	@Override
	public SocketHandler createSocketHandler(Socket socket) {
		return new HttpSocketHandler(socket, this.responsesConfig);
	}
}
