package com.example.lab006;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    File selectedFile;
    DataObject selectedDataObject;
    FileProcessor fileProcessor = new FileProcessor();
    @FXML
    private Label fileInfo;
    @FXML
    private Label elementInfo;
    @FXML
    private Button fileButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button clearButton;
    @FXML
    private TableView<DataObject> leftTable;
    @FXML
    private TableView<DataObject> readTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable(leftTable, fileProcessor.leftData);
        populateTable(readTable, fileProcessor.readData);
    }

    public void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedFile = file;
            fileInfo.setText("Reading from: " + selectedFile.getPath());
            fileProcessor.setSelectedFile(selectedFile);
            Thread thread = new Thread(fileProcessor::readFile);
            thread.start();
        }
    }

    public void populateTable(TableView tableView, ObservableList<DataObject> data) {
        TableColumn<DataObject, String> firstNameCol = new TableColumn<>("First name");
        TableColumn<DataObject, String> lastNameCol = new TableColumn<>("Last name");
        TableColumn<DataObject, String> emailCol = new TableColumn<>("Email");
        TableColumn<DataObject, String> imageLinkCol = new TableColumn<>("Image link");
        TableColumn<DataObject, String> ipCol = new TableColumn<>("IP");

        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        imageLinkCol.setCellValueFactory(new PropertyValueFactory<>("imageLink"));
        ipCol.setCellValueFactory(new PropertyValueFactory<>("ip"));

        firstNameCol.setReorderable(false);
        firstNameCol.setSortable(false);
        lastNameCol.setReorderable(false);
        lastNameCol.setSortable(false);
        emailCol.setReorderable(false);
        emailCol.setSortable(false);
        imageLinkCol.setReorderable(false);
        imageLinkCol.setSortable(false);
        ipCol.setReorderable(false);
        ipCol.setSortable(false);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(firstNameCol, lastNameCol, emailCol, imageLinkCol, ipCol);
        tableView.setItems(data);
    }

    public void clear(){
        Thread thread = new Thread(fileProcessor :: clearData);
        thread.start();
    }

    public void getSelectedElement() {
        selectedDataObject = readTable.getSelectionModel().getSelectedItem();
        if (selectedDataObject != null) {
            elementInfo.setText(selectedDataObject.toString());
        }
    }

    public void removeSelectedElement() {
        if (selectedDataObject != null) {
            Thread thread = new Thread(() -> fileProcessor.removeElement(selectedDataObject));
            thread.start();
        }
    }


}
