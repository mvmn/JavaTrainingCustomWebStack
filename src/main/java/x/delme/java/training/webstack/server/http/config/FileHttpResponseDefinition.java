package x.delme.java.training.webstack.server.http.config;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

public class FileHttpResponseDefinition extends AbstractHttpResponseDefinition {
	protected final String filePath;

	public FileHttpResponseDefinition(int statusCode, String reasonPhrase, String filePath) {
		super(statusCode, reasonPhrase);
		this.filePath = filePath;
	}

	@Override
	public String getContent() {
		try {
			String result = null;

			File file = new File(filePath);
			if (file.exists() && !file.isDirectory()) {
				result = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			} else {
				throw new RuntimeException("File not found at " + filePath);
			}

			return result;
		} catch (Exception e) {
			throw new RuntimeException("Failed to retrieve file content from " + filePath, e);
		}
	}
}