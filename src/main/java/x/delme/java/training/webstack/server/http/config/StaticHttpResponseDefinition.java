package x.delme.java.training.webstack.server.http.config;

public class StaticHttpResponseDefinition extends AbstractHttpResponseDefinition {
	protected final String content;

	public StaticHttpResponseDefinition(int statusCode, String reasonPhrase, String content) {
		super(statusCode, reasonPhrase);
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}
}