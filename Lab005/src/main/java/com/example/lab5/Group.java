package com.example.lab5;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Group {

    public String name;
    public ObservableList<String> users = FXCollections.observableArrayList();
    public ObservableList<ClientHandler> clients = FXCollections.observableArrayList();
    public ArrayList<Message> messages;
    public boolean selected = false;
    TextArea chatArea;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
        File file = new File("groups/" + name + ".json");
        if (file.exists()) {
            Gson gson = new Gson();
            try (Reader reader = new FileReader(file)) {
                messages = gson.fromJson(reader, new TypeToken<List<Message>>() {
                }.getType());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(messages == null) messages = new ArrayList<>();
        if(users == null) users = FXCollections.observableArrayList();
        if(clients == null) clients = FXCollections.observableArrayList();
    }

    public void addMessage(Message message) {
        System.out.println("Adding message to group: " + name);
        messages.add(message);

        Gson gson = new Gson();
        String json = gson.toJson(messages);
        File file = new File("groups/" + name + ".json");
        try {
            java.io.FileWriter fileWriter = new java.io.FileWriter(file);
            gson.newJsonWriter(fileWriter).jsonValue(json);
            fileWriter.close();
            if(selected) showMessages(chatArea);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + file.getName());
        }
    }

    public void showMessages(TextArea chatArea) {
        this.chatArea = chatArea;
        chatArea.clear();
        if(messages == null || messages.isEmpty()) return;

        for (Message message : messages) {
            if (message.getType() == Message.MessageType.MESSAGE) {
                chatArea.appendText(message.getUsername() + ": " + message.getContent() + "\n");
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
    public String getName() {
        return name;
    }
    public ObservableList<String> getUsers() {
        return users;
    }
    public ObservableList<ClientHandler> getClients() {
        return clients;
    }
}
