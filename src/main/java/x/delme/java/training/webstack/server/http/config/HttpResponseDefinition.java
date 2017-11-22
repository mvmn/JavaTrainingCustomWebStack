package x.delme.java.training.webstack.server.http.config;

public interface HttpResponseDefinition {
	public int getStatusCode();

	public String getReasonPhrase();

	public String getContent();
}