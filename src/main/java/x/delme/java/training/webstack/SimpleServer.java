package x.delme.java.training.webstack;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import x.delme.java.training.webstack.server.SocketHandlerFactory;
import x.delme.java.training.webstack.server.http.HttpSocketHandlerFactory;

public class SimpleServer {

	public static void main(String args[]) throws Exception {
		int port = 8080;
		String httpRoutesConfig;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		if (args.length > 1) {
			File configFile = new File(args[1]);
			if (configFile.exists() && !configFile.isDirectory()) {
				httpRoutesConfig = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
			} else {
				throw new RuntimeException("Config file does not exist at: " + args[1]);
			}
		} else {
			httpRoutesConfig = IOUtils.toString(SimpleServer.class.getResourceAsStream("/default_routes"), StandardCharsets.UTF_8);
		}
		new SimpleServer(port, new HttpSocketHandlerFactory(httpRoutesConfig)).start();
	}

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
