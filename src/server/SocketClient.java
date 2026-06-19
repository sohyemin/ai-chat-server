package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    public void connect() throws IOException {

        Socket socket = new Socket("localhost", 8080);
        System.out.println("서버 연결 성공!");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        PrintWriter writer = new PrintWriter(
                socket.getOutputStream(),
                true
        );

        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("메시지 입력 : ");
            String message = scanner.nextLine();

            writer.println(message);

            System.out.println("서버 응답 : ");

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.equals("[END]")) {
                    break;
                }

                System.out.println(line);
            }

            if (message.equals("exit")) {
                break;
            }

        }
        socket.close();
    }
}
