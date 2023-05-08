package com.example.lab5;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;

    private TextArea chatArea;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error creating client");
            close();
        }
    }

    public void connect(String group) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("Connected to server");
                Message connectMessage = new Message();
                connectMessage.setType(Message.MessageType.CONNECT);
                connectMessage.setUsername(username);
                connectMessage.setGroup(group);
                sendMessage(connectMessage);
            }
        }).start();
    }

    public void sendMessage(Message message) {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        try {
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error sending message to server");
            close();
        }
    }

    public void receiveMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                try {
                    while (socket.isConnected()) {
                        message = reader.readLine();
                        // Parse message from json
                        Gson gson = new Gson();
                        Message messageObject = gson.fromJson(message, Message.class);

                        switch (messageObject.getType()) {
                            case CONNECT -> {
                                Platform.runLater(() -> {
                                    chatArea.appendText("User " + messageObject.getUsername() + " connected\n");
                                });
                            }

                            case MESSAGE -> {
                                Platform.runLater(() -> {
                                    chatArea.appendText(messageObject.getUsername() + ": " + messageObject.getContent() + "\n");
                                });
                            }

                            case PRIVATE_MESSAGE -> {
                                if(messageObject.receiver.equals(username)) {
                                    Platform.runLater(() -> {
                                        chatArea.appendText(messageObject.getUsername() + " (private): " + messageObject.getContent() + "\n");
                                    });
                                } else if (messageObject.getUsername().equals(username)) {
                                    Platform.runLater(() -> {
                                        chatArea.appendText("Message(private) to " + messageObject.getReceiver() + ": " + messageObject.getContent() + "\n");
                                    });
                                }
                            }

                            case DISCONNECT -> {
                                Platform.runLater(() -> {
                                    chatArea.appendText("User " + messageObject.getUsername() + " disconnected\n");
                                });
                            }

                            case ERROR -> {
                                chatArea.setDisable(true);
                                close();
                                Platform.runLater(() -> {
                                    chatArea.appendText("Error: " + messageObject.getContent() + "\n");
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading message from server");
                    close();
                }
            }
        }).start();
    }

    public void close() {
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing client");
        }
    }

    public void setChatArea(TextArea chatArea) {
        this.chatArea = chatArea;
    }
}
