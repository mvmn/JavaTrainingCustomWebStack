package x.delme.java.training.webstack.server.http.config;

public class HttpResponseConfigEntry {
	protected final String method;
	protected final String path;
	protected final HttpResponseDefinition responseDefinition;

	public HttpResponseConfigEntry(String method, String path, HttpResponseDefinition responseDefinition) {
		this.method = method;
		this.path = path;
		this.responseDefinition = responseDefinition;
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public HttpResponseDefinition getResponseDefinition() {
		return responseDefinition;
	}
}