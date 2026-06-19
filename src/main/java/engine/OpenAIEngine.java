package engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Properties;

public class OpenAIEngine implements AIEngine{

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Dotenv dotenv =
            Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

    private final String apiKey =
            dotenv.get("OPENAI_API_KEY");

    @Override
    public String chat(String message) {
        if (apiKey == null || apiKey.isBlank()) {
            return "API key가 없습니다";
        }

        try {
            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-4.1-mini",
                    "input", message
            );

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/responses"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return parseResponse(response.body());

        } catch (IOException | InterruptedException e){
            return "OpenAI 호출 중 오류 발생: " + e.getMessage();
        }
    }

    private String parseResponse(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);

        if(root.has("error")){

            JsonNode error = root.get("error");

            return String.format(
                    "[OpenAI 오류]\ncode: %s\nmessage: %s",
                    error.get("code").asText(),
                    error.get("message").asText()
            );
        }

        StringBuilder result = new StringBuilder();

        JsonNode output = root.get("output");

        if (output != null && output.isArray()) {
            for (JsonNode item : output) {
                JsonNode content = item.get("content");

                if (content != null && content.isArray()) {
                    for (JsonNode contentItem : content) {
                        if (contentItem.has("text")) {
                            result.append(contentItem.get("text").asText());
                        }
                    }
                }
            }
        }

        if (result.isEmpty()) {
            return "OpenAI 응답 파싱 실패: " + responseBody;
        }

        return result.toString();
    }
}
