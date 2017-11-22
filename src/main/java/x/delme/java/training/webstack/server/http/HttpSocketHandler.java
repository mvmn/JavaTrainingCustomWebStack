package x.delme.java.training.webstack.server.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import x.delme.java.training.webstack.server.SocketHandler;
import x.delme.java.training.webstack.server.http.config.HttpResponseDefinition;
import x.delme.java.training.webstack.server.http.config.HttpResponsesConfig;

public class HttpSocketHandler extends SocketHandler {

	private static final byte[] RESPONSE_BYTES_404 = "HTTP/1.1 404 Not Found\r\n\r\nPage not found\r\n\r\n".getBytes(StandardCharsets.UTF_8);

	protected final HttpResponsesConfig responsesConfig;

	public HttpSocketHandler(Socket socket, HttpResponsesConfig responsesConfig) {
		super(socket);
		this.responsesConfig = responsesConfig;
	}

	@Override
	public void run() {
		System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());
		try {
			List<String> inputLines = new ArrayList<>();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			String line = "skip";
			while (line != null && !line.isEmpty()) {
				line = bufferedReader.readLine();
				inputLines.add(line);
			}

			String[] firstLineParts = inputLines.get(0).split(" ");
			String method = firstLineParts[0];
			String uri = firstLineParts[1];
			String path;
			String queryString;
			int indexOfQuestionMark = uri.indexOf("?");
			if (indexOfQuestionMark > -1) {
				path = uri.substring(0, indexOfQuestionMark);
				queryString = uri.substring(indexOfQuestionMark + 1);
			} else {
				path = uri;
				queryString = null;
			}
			System.out.println("Received request " + method + " to " + path + ". " + (queryString != null ? "Query string: " + queryString : ""));

			HttpResponseDefinition responseDefinition = this.responsesConfig.getResponseDefiniton(method, path);

			byte[] response = RESPONSE_BYTES_404;
			if (responseDefinition != null) {
				StringBuilder responseStr = new StringBuilder("HTTP/1.1 ");
				responseStr.append(responseDefinition.getStatusCode()).append(" ").append(responseDefinition.getReasonPhrase()).append("\r\n\r\n");
				String content = responseDefinition.getContent();
				if (content != null) {
					responseStr.append(content).append("\r\n\r\n");
				}
				response = responseStr.toString().getBytes(StandardCharsets.UTF_8);
			}
			socket.getOutputStream().write(response);

			socket.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
