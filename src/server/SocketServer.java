package server;

import engine.AIEngine;
import factory.AIEngineFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private static final String END_SIGNAL = "[END]";

    public void start() throws IOException {

        AIEngine engine = AIEngineFactory.create("openAi");
//        AIEngine engine = AIEngineFactory.create("ollama");

        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("서버 시작!");

        Socket socket = serverSocket.accept();
        System.out.println("클라이언트 연결!");

        BufferedReader reader = new BufferedReader(
          new InputStreamReader(socket.getInputStream())
        );

        PrintWriter writer = new PrintWriter(
                socket.getOutputStream(),
                true
        );

        String message;

        while ((message = reader.readLine()) !=null){
            if(message.equals("exit")){
                writer.println("채팅을 종료합니다.");
                writer.println(END_SIGNAL);
                break;
            }

            if(message.equals("/openai")){
                engine =
                        AIEngineFactory.create("openAi");
                writer.println("OpenAI로 변경되었습니다.");
                writer.println(END_SIGNAL);
                continue;
            }
            if(message.equals("/ollama")){
                engine =
                        AIEngineFactory.create("ollama");
                writer.println("Ollama로 변경되었습니다.");
                writer.println(END_SIGNAL);
                continue;
            }

            String response = engine.chat(message);

            writer.println(response);
            writer.println(END_SIGNAL);
        }

        socket.close();
        serverSocket.close();
    }

}