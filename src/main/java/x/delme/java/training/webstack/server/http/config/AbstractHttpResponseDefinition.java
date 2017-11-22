package x.delme.java.training.webstack.server.http.config;

public abstract class AbstractHttpResponseDefinition implements HttpResponseDefinition {
	protected final int statusCode;
	protected final String reasonPhrase;

	public AbstractHttpResponseDefinition(int statusCode, String reasonPhrase) {
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}
}