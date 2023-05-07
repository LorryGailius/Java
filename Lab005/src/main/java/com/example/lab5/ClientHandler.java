package com.example.lab5;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private static ObservableList<Group> groups = FXCollections.observableArrayList();
    Group currentGroup = null;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    public String username;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error creating client handler");
        }
    }

    public static ObservableList<Group> getGroups() {
        return groups;
    }

    public static void setGroups(ObservableList<Group> groups) {
        ClientHandler.groups = groups;
    }

    @Override
    public void run() {
        String message;

        while (!socket.isClosed()) {
            try {
                message = reader.readLine();
                Gson gson = new Gson();
                Message messageObject = gson.fromJson(message, Message.class);
                this.username = messageObject.getUsername();

                switch (messageObject.getType()) {
                    case CONNECT -> {
                        if (getCurrentGroup(messageObject.getGroup()) == null) {
                            Group group = new Group(messageObject.getGroup());
                            groups.add(group);
                        }

                        currentGroup = getCurrentGroup(messageObject.getGroup());

                        if (!currentGroup.users.contains(username)) {
                            currentGroup.users.add(messageObject.getUsername());
                            currentGroup.clients.add(this);
                        } else {
                            Message errorMessage = new Message();
                            errorMessage.setType(Message.MessageType.ERROR);
                            errorMessage.setUsername(username);
                            errorMessage.setGroup(messageObject.getGroup());
                            errorMessage.setContent("User with this name already exists in group");
                            sendMessage(errorMessage);
                            close();
                        }

                        System.out.println("User " + username + " connected to " + messageObject.getGroup());
                        this.username = messageObject.getUsername();
                        for (ClientHandler client : currentGroup.clients) {
                            if (!client.equals(this)) {
                                client.sendMessage(messageObject);
                            }
                        }

                        // Send all messages in group to new client
                        if (currentGroup.messages != null && currentGroup.messages.size() > 0) {
                            int count = 0;
                            for (Message groupMessage : currentGroup.messages) {
                                if (groupMessage.getType() == Message.MessageType.MESSAGE || groupMessage.getType() == Message.MessageType.PRIVATE_MESSAGE) {
                                    sendMessage(groupMessage);
                                    count++;
                                }
                            }
                            System.out.println("Sent " + count + " messages to " + username);
                        }
                    }

                    case MESSAGE -> {
                        currentGroup = getCurrentGroup(messageObject.getGroup());
                        currentGroup.addMessage(messageObject);
                        for (ClientHandler client : currentGroup.clients) {
                            client.sendMessage(messageObject);
                        }
                    }

                    case PRIVATE_MESSAGE -> {
                        currentGroup = getCurrentGroup(messageObject.getGroup());
                        for (ClientHandler client : currentGroup.clients) {
                            if (client.username.equals(messageObject.getReceiver()) || client.username.equals(messageObject.getUsername())) {
                                client.sendMessage(messageObject);
                                currentGroup.addMessage(messageObject);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                close();
                break;
            }
        }

        if (socket.isClosed()) {
            currentGroup.clients.remove(this);
            currentGroup.users.remove(username);
            System.out.println("User " + username + " disconnected from " + currentGroup.name);
        }
    }

    public void sendMessage(Message message) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(message);
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error sending message to client");
            System.out.println(e.getMessage());
            close();
        }
    }

    public Group getCurrentGroup(String group) {
        return groups.stream().filter(g -> g.name.equals(group)).findFirst().orElse(null);
    }

    public void close() {
        try {
            socket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error closing client handler");
        }
    }
}
