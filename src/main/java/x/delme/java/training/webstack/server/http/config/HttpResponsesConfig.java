package x.delme.java.training.webstack.server.http.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponsesConfig {

	public static HttpResponsesConfig parseConfig(String httpResponsesConfigStr) {
		HttpResponsesConfig result = new HttpResponsesConfig();
		for (String line : httpResponsesConfigStr.split("\n")) {
			int firstSpaceIndex = line.indexOf(" ");
			int secondSpaceIndex = line.indexOf(" ", firstSpaceIndex + 1);
			int colonIndex = line.indexOf(":", secondSpaceIndex + 1);
			String method = line.substring(0, firstSpaceIndex);
			String path = line.substring(firstSpaceIndex + 1, secondSpaceIndex);
			String type = line.substring(secondSpaceIndex + 1, colonIndex);
			String responseDefinitionStr = line.substring(colonIndex + 1);

			HttpResponseDefinition responseDefinition;
			if ("static".equalsIgnoreCase(type)) {
				int respDefFirstSpaceIndex = responseDefinitionStr.indexOf(" ");
				int respDefSecondSpaceIndex = responseDefinitionStr.indexOf(" ", respDefFirstSpaceIndex + 1);

				int statusCode = Integer.parseInt(responseDefinitionStr.substring(0, respDefFirstSpaceIndex));
				String reasonPhrase = responseDefinitionStr.substring(respDefFirstSpaceIndex + 1, respDefSecondSpaceIndex);
				String content = responseDefinitionStr.substring(respDefSecondSpaceIndex + 1);

				responseDefinition = new StaticHttpResponseDefinition(statusCode, reasonPhrase, content);
			} else if ("file".equalsIgnoreCase(type)) {
				int respDefFirstSpaceIndex = responseDefinitionStr.indexOf(" ");
				int respDefSecondSpaceIndex = responseDefinitionStr.indexOf(" ", respDefFirstSpaceIndex + 1);

				int statusCode = Integer.parseInt(responseDefinitionStr.substring(0, respDefFirstSpaceIndex));
				String reasonPhrase = responseDefinitionStr.substring(respDefFirstSpaceIndex + 1, respDefSecondSpaceIndex);
				String filePath = responseDefinitionStr.substring(respDefSecondSpaceIndex + 1);

				responseDefinition = new FileHttpResponseDefinition(statusCode, reasonPhrase, filePath);
			} else {
				throw new RuntimeException("Unknown HTTP response type definition " + type);
			}

			result.addResponseConfigEntry(new HttpResponseConfigEntry(method, path, responseDefinition));
		}
		return result;
	}

	protected final List<HttpResponseConfigEntry> configEntries = new ArrayList<>();
	protected final Map<String, Map<String, HttpResponseConfigEntry>> configEntriesByMethodThenByPath = new HashMap<>();

	public void addResponseConfigEntry(HttpResponseConfigEntry entry) {
		synchronized (this) {
			configEntries.add(entry);
			Map<String, HttpResponseConfigEntry> configEntriesByPath = configEntriesByMethodThenByPath.get(entry.getMethod());
			if (configEntriesByPath == null) {
				configEntriesByPath = new HashMap<>();
				configEntriesByMethodThenByPath.put(entry.getMethod(), configEntriesByPath);
			}
			configEntriesByPath.put(entry.getPath(), entry);
		}
	}

	public HttpResponseDefinition getResponseDefiniton(String method, String path) {
		Map<String, HttpResponseConfigEntry> configEntriesByPath = configEntriesByMethodThenByPath.get(method);
		if (configEntriesByPath != null) {
			HttpResponseConfigEntry configEntry = configEntriesByPath.get(path);
			return configEntry != null ? configEntry.getResponseDefinition() : null;
		} else {
			return null;
		}
	}
}
