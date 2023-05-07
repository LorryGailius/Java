package com.example.lab5;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    static int port = -1;
    static ObservableList<Group> groups = ClientHandler.getGroups();
    static Server server = null;
    private Group selectedGroup = null;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField portField;
    @FXML
    private ComboBox<Group> groupSelector;
    @FXML
    private TableView<String> userTable;
    @FXML
    private TextArea chatArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (server != null && server.isRunning()) {
            groupSelector.setItems(groups);
            groupSelector.setOnAction(event -> groupSelected());
            infoLabel.setText("Server created on port " + port);

            // Create columns
            TableColumn<String, String> usernameColumn = new TableColumn<>("Username");
            usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
            usernameColumn.prefWidthProperty().bind(userTable.widthProperty());
            userTable.getColumns().add(usernameColumn);
            userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Resize columns to fill the table view width
        }
    }

    public void StartServer() {

        if (portField.getText().isEmpty()) {
            System.out.println("Port field is empty");
            return;
        }

        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Port field is not a number");
            return;
        }

        if (port < 0 || port > 65535) {
            System.out.println("Port field is not in range");
            return;
        }

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            server = new Server(serverSocket);
            Thread thread = new Thread(server::start);
            thread.start();
        } catch (IOException e) {
            System.out.println("Error creating server");
            return;
        }
        changeScene();
    }

    public void changeScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("server-view.fxml"));
            Stage stage = (Stage) portField.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setOnCloseRequest(event -> {
                System.out.println("Closing");
                // Add client.close to new thread
                Thread thread = new Thread(server :: stop);
                thread.start();

                Platform.exit();
                System.exit(0);

            });
            stage.setScene(scene);
        } catch (IOException io) {
            System.out.println("Error changing scene");
        }
    }

    public void groupSelected() {
        if (selectedGroup != null) {
            selectedGroup.selected = false;
        }
        selectedGroup = groupSelector.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            return;
        }

        userTable.setItems(selectedGroup.users);
        userTable.refresh();
        selectedGroup.selected = true;
        selectedGroup.showMessages(chatArea);
    }

    public void handleEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == keyEvent.getCode().ENTER) {
            if(server == null) {
                StartServer();
                return;
            }
        }
    }
}
