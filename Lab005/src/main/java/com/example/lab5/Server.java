package com.example.lab5;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        File folder = new File("groups");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    String name = file.getName().replace(".json", "");
                    Group group = new Group(name);
                    ClientHandler.getGroups().add(group);
                }
            }
        }
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Error creating server");
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing server");
        }
    }

    public boolean isRunning() {
        return !serverSocket.isClosed();
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Server server = new Server(serverSocket);

            // Start server.start in a new thread
            Thread thread = new Thread(server::start);
            thread.start();

            // Wait for user input
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                if (input.equals("exit")) {
                    server.stop();
                    break;
                } else if (input.equals("groups")) {
                    if (ClientHandler.getGroups().size() == 0) {
                        System.out.println("No groups created yet");
                    }

                    for (Group group : ClientHandler.getGroups()) {
                        System.out.println(group);
                    }
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
