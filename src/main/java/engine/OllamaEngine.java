package engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class OllamaEngine implements AIEngine{

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String model = "gemma4";

    @Override
    public String chat(String message) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "prompt", message,
                    "stream", false
            );

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return parseResponse(response.body());

        } catch (IOException | InterruptedException e) {
            return "Ollama 호출 중 오류 발생: " + e.getMessage();
        }
    }

    private String parseResponse(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);

        if (root.has("error")) {
            return "Ollama 오류: " + root.get("error").asText();
        }

        if (!root.has("response")) {
            return "Ollama 응답에 response 필드가 없습니다: " + responseBody;
        }

        return root.get("response").asText();
    }
}
