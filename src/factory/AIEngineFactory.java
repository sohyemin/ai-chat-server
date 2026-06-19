package factory;

import engine.*;

public class AIEngineFactory {

    public static AIEngine create(String type){

        if(type.equals("openAi")){
            return new OpenAIEngine();
        }

        return new OllamaEngine();

    }
}
