package com.example.lab009;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HelloController implements Initializable {
    boolean preserveOriginalSelected = false;

    File selectedFile;
    ObservableList<DataObject> data = FXCollections.observableArrayList();
    Map<String, DataObject> map;
    @FXML
    private CheckBox preserveOriginal;
    @FXML
    private Label fileInfo;
    @FXML
    private Label sizeInfo;
    @FXML
    private TextField firstNameFilter;
    @FXML
    private TextField lastNameFilter;
    @FXML
    private TextField emailFilter;
    @FXML
    private TextField imageLinkFilter;
    @FXML
    private TextField ipFilter;
    @FXML
    private TableView<DataObject> table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable(table, data);
        sizeInfo.setText("Size: " + data.stream().count());
        data.addListener((javafx.collections.ListChangeListener.Change<? extends DataObject> c) -> {
            sizeInfo.setText("Size: " + data.stream().count());
        });
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

    public void changePreservation() {
        if (preserveOriginal.isSelected()) {
            preserveOriginalSelected = true;
        } else {
            preserveOriginalSelected = false;
        }
    }

    public void selectFileRead() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedFile = file;
            fileInfo.setText("Reading from: " + selectedFile.getPath());
            readFile(selectedFile);
        }
        table.setItems(data);
        table.refresh();
    }

    public void selectFileWrite() {
        ObservableList<DataObject> dataToWrite = table.getItems();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write("first_name;last_name;email;imagelink;ip_address\n");
                for (DataObject dataObject : dataToWrite) {
                    writer.write(dataObject.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sortAscending() {
        if (data.stream().count() == 0) {
            return;
        }

        if (!preserveOriginalSelected) {
            data = data.stream()
                    .sorted(Comparator.comparing(DataObject::getFirst_name, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            table.setItems(data);
            table.refresh();
            return;
        }

        ObservableList<DataObject> filteredData = data.stream()
                .sorted(Comparator.comparing(DataObject::getFirst_name, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        table.setItems(filteredData);
        table.refresh();
    }

    public void sortDescending() {
        if (data.stream().count() == 0) {
            return;
        }

        if (!preserveOriginalSelected) {
            data = data.stream()
                    .sorted(Comparator.comparing(DataObject::getFirst_name, String.CASE_INSENSITIVE_ORDER).reversed())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            table.setItems(data);
            table.refresh();
            return;
        }

        ObservableList<DataObject> filteredData = data.stream()
                .sorted(Comparator.comparing(DataObject::getFirst_name, String.CASE_INSENSITIVE_ORDER).reversed())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        table.setItems(filteredData);
        table.refresh();
    }

    public void makeUppercase() {
        if (data.stream().count() == 0) {
            return;
        }

        if (!preserveOriginalSelected) {
            data = data.stream()
                    .map(dataObject -> {
                        String upperFirstName = dataObject.getFirst_name().toUpperCase();
                        String upperLastName = dataObject.getLast_name().toUpperCase();
                        return new DataObject(upperFirstName, upperLastName, dataObject.getEmail(), dataObject.getImageLink(), dataObject.getIp());
                    })
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            table.setItems(data);
            table.refresh();
            return;
        }

        ObservableList<DataObject> filteredData = data.stream()
                .map(dataObject -> {
                    String upperFirstName = dataObject.getFirst_name().toUpperCase();
                    String upperLastName = dataObject.getLast_name().toUpperCase();
                    return new DataObject(upperFirstName, upperLastName, dataObject.getEmail(), dataObject.getImageLink(), dataObject.getIp());
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        table.setItems(filteredData);
        table.refresh();
    }

    public void makeLowercase() {
        if (data.stream().count() == 0) {
            return;
        }

        if (!preserveOriginalSelected) {
            data = data.stream()
                    .map(dataObject -> {
                        String lowerFirstName = dataObject.getFirst_name().toLowerCase();
                        String lowerLastName = dataObject.getLast_name().toLowerCase();
                        return new DataObject(lowerFirstName, lowerLastName, dataObject.getEmail(), dataObject.getImageLink(), dataObject.getIp());
                    })
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            table.setItems(data);
            table.refresh();
            return;
        }

        ObservableList<DataObject> filteredData = data.stream()
                .map(dataObject -> {
                    String lowerFirstName = dataObject.getFirst_name().toLowerCase();
                    String lowerLastName = dataObject.getLast_name().toLowerCase();
                    return new DataObject(lowerFirstName, lowerLastName, dataObject.getEmail(), dataObject.getImageLink(), dataObject.getIp());
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        table.setItems(filteredData);
        table.refresh();
    }

    public void filter() {
        if (data.stream().count() == 0) {
            return;
        }

        if (!preserveOriginalSelected) {
            data = data.stream()
                    .filter(dataObject -> dataObject.getFirst_name().toLowerCase().contains(firstNameFilter.getText().toLowerCase()))
                    .filter(dataObject -> dataObject.getLast_name().toLowerCase().contains(lastNameFilter.getText().toLowerCase()))
                    .filter(dataObject -> dataObject.getEmail().toLowerCase().contains(emailFilter.getText().toLowerCase()))
                    .filter(dataObject -> dataObject.getImageLink().toLowerCase().contains(imageLinkFilter.getText().toLowerCase()))
                    .filter(dataObject -> dataObject.getIp().toLowerCase().contains(ipFilter.getText().toLowerCase()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            table.setItems(data);
            table.refresh();
            return;
        }

        ObservableList<DataObject> filteredData = data.stream()
                .filter(dataObject -> dataObject.getFirst_name().toLowerCase().contains(firstNameFilter.getText().toLowerCase()))
                .filter(dataObject -> dataObject.getLast_name().toLowerCase().contains(lastNameFilter.getText().toLowerCase()))
                .filter(dataObject -> dataObject.getEmail().toLowerCase().contains(emailFilter.getText().toLowerCase()))
                .filter(dataObject -> dataObject.getImageLink().toLowerCase().contains(imageLinkFilter.getText().toLowerCase()))
                .filter(dataObject -> dataObject.getIp().toLowerCase().contains(ipFilter.getText().toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        table.setItems(filteredData);
        table.refresh();
    }

    public void clearFilter() {
        if(!preserveOriginalSelected)
        {
            readFile(selectedFile);
        }

        table.setItems(data);
        table.refresh();
    }

    public void createMap() {
        if (data.stream().count() == 0) {
            return;
        }

        map = data.stream().collect(Collectors.toMap(dataObject -> dataObject.getIp(), dataObject -> dataObject));

        for (Map.Entry<String, DataObject> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public void readFile(File file)
    {
        data.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            // Read first line and ignore it
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parsedData = line.split(";");
                DataObject dataObject = new DataObject(parsedData[0], parsedData[1], parsedData[2], parsedData[3], parsedData[4]);
                data.add(dataObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}