package engine;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OllamaEngine implements AIEngine{

    private final HttpClient client = HttpClient.newHttpClient();
    private final String model = "gemma4";

    @Override
    public String chat(String message) {
        try {
            String requestBody = """
                    {
                    "model": "%s",
                    "prompt": "%s",
                    "stream": false
                    }
                    """.formatted(model, escapeJson(message));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return extraResponse(response.body());
        } catch (IOException | InterruptedException e) {
            return "Ollama 호출 중 오류 발생: " + e.getMessage();
        }
    }

    private String extraResponse(String json){
        if (json.contains("\"error\"")) {
            return "Ollama 오류: " + json;
        }

        String key = "\"response\":\"";
        int start = json.indexOf(key);

        if(start == -1){
            return "응답 파싱 실패: "+ json;
        }

        start += key.length();
        int end = json.indexOf("\",", start);

        if(end == -1){
            return "응답 파싱 실패: " + json;
        }

        return json.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }

    private String escapeJson(String text){
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
