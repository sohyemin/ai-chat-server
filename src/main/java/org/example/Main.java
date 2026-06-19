package org.example;

import server.SocketServer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception{

        SocketServer server = new SocketServer();

        server.start();
    }
}