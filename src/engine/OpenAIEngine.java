package engine;

import java.io.FileInputStream;
import java.net.http.HttpClient;
import java.util.Properties;

public class OpenAIEngine implements AIEngine{

    private final HttpClient client = HttpClient.newHttpClient();

    Properties properties = new Properties();

    try(FileInputStream fis =
        new FileInputStream("env.properties")) {
        properties.load(fis);
    }

    String apiKey = properties.getProperty("OPENAI_API_KEY");

    @Override
    public String chat(String message) {
        return "openAI : " + message;
    }
}
