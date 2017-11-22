package x.delme.java.training.webstack;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import x.delme.java.training.webstack.server.ServerConfig;
import x.delme.java.training.webstack.server.SocketHandler;

public class SimpleServer {

	public static void main(String args[]) throws Exception {
		int port = 8080;
		ServerConfig config;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		if (args.length > 1) {
			File configFile = new File(args[1]);
			if (configFile.exists() && !configFile.isDirectory()) {
				config = new ServerConfig(FileUtils.readFileToString(configFile, StandardCharsets.UTF_8));
			} else {
				throw new RuntimeException("Config file does not exist at: " + args[1]);
			}
		} else {
			config = new ServerConfig(IOUtils.toString(SimpleServer.class.getResourceAsStream("/default_routes"), StandardCharsets.UTF_8));
		}
		new SimpleServer(port, config).start();
	}

	protected final int port;
	protected final ServerConfig config;
	protected final Object LOCK = new Object();
	protected volatile ServerSocket serverSocket;

	public SimpleServer(int port, ServerConfig config) {
		this.port = port;
		this.config = config;
	}

	public void start() throws IOException {
		synchronized (LOCK) {
			if (this.serverSocket == null) {
				this.serverSocket = new ServerSocket(port);
				System.out.println("Listening at port " + port + "...");
				while (!this.serverSocket.isClosed()) {
					new Thread(new SocketHandler(this.serverSocket.accept()) {
						@Override
						public void run() {
							System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());
							try {
								StringBuilder inputContent = new StringBuilder();
								BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
								String line = "skip";
								while (line != null && !line.isEmpty()) {
									line = bufferedReader.readLine();
									inputContent.append(line).append("\n");
								}
								System.out.println(".....\nInput: \n" + inputContent.toString() + "'''''\n");
								socket.getOutputStream().write("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
								socket.close();
							} catch (Throwable t) {
								t.printStackTrace();
							}
						}
					}).start();
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
