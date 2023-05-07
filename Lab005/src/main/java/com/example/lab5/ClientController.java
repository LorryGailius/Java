package com.example.lab5;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private static Client client;

    private static String group;

    private static String username;

    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextField usernameField;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField groupField;
    @FXML
    private TextField messageField;
    @FXML
    private TextArea chatArea;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(client != null) {
            usernameLabel.setText(username);
            chatArea.appendText("Connected to group " + group + "\n");
            client.setChatArea(chatArea);
        }
    }

    public void connectToServer() {

        if (usernameField.getText().isEmpty() || groupField.getText().isEmpty() || usernameField.getText().contains(" ")) {
            System.out.println("Username or group field is empty or contains spaces");
            return;
        }

        if (hostField.getText().isEmpty() || portField.getText().isEmpty()) {
            hostField.setText("localhost");
            portField.setText("8080");
        }

        try {
            Integer.parseInt(portField.getText());
        } catch (Exception e) {
            System.out.println("Port must be a number");
            return;
        }

        if (Integer.parseInt(portField.getText()) < 0 || Integer.parseInt(portField.getText()) > 65535) {
            System.out.println("Port must be in range 0-65535");
            return;
        }

        try {
            Socket socket = new Socket(hostField.getText(), Integer.parseInt(portField.getText()));
            username = usernameField.getText();
            group = groupField.getText();
            client = new Client(socket, usernameField.getText());
            System.out.println("Client created");
            client.receiveMessage();
            client.connect(groupField.getText());
            changeScene();
        } catch (Exception e) {
            System.out.println("Error connecting to server");
        }
    }

    public void changeScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("client-view.fxml"));
            Stage stage = (Stage) portField.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setOnCloseRequest(event -> {
                System.out.println("Closing");
                // Add client.close to new thread
                Thread thread = new Thread(client :: close);
                thread.start();

                Platform.exit();
                System.exit(0);

            });
            stage.setScene(scene);
        } catch (IOException io) {
            System.out.println("Error changing scene");
        }
    }

    public void send() {
        if (client == null) {
            System.out.println("You are not connected to a server");
            return;
        }

        if (messageField.getText().isEmpty()) {
            System.out.println("Message cannot be empty");
            return;
        }

        if (messageField.getText().length() > 255) {
            System.out.println("Message cannot be longer than 255 characters");
            return;
        }

        if (messageField.getText().charAt(0) == '@' && messageField.getText().charAt(1) != ' ') {
            Message message = new Message();
            message.setType(Message.MessageType.PRIVATE_MESSAGE);
            message.setContent(messageField.getText().substring(messageField.getText().indexOf(' ') + 1));
            message.setUsername(username);
            message.setGroup(group);
            message.setReceiver(messageField.getText().substring(1, messageField.getText().indexOf(' ')));
            client.sendMessage(message);
            messageField.clear();
            return;
        }

        Message message = new Message();
        message.setType(Message.MessageType.MESSAGE);
        message.setContent(messageField.getText());
        message.setUsername(username);
        message.setGroup(group);
        client.sendMessage(message);
        messageField.clear();
    }

    public void handleEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == keyEvent.getCode().ENTER) {
            if(client == null) {
                connectToServer();
                return;
            }
            send();
        }
    }


}
