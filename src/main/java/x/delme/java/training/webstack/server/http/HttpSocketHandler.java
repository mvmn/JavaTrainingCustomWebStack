package x.delme.java.training.webstack.server.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import x.delme.java.training.webstack.server.SocketHandler;

public class HttpSocketHandler extends SocketHandler {

	public HttpSocketHandler(Socket socket) {
		super(socket);
	}

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
			socket.getOutputStream().write("HTTP/1.1 200 OK\r\n\r\nHello World\r\n\r\n".getBytes(StandardCharsets.UTF_8));
			socket.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
