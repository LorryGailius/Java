package com.example.lab006;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileProcessor {
    public ObservableList<DataObject> leftData = FXCollections.observableArrayList();
    public ObservableList<DataObject> readData = FXCollections.observableArrayList();
    public File selectedFile;

    public void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String line;
            // Read first line and ignore it
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                readLine(line);
                Thread.sleep(500);
            }
            System.out.println("Done reading file");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void readLine(String line) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] data = line.split(";");
                DataObject dataObject = new DataObject(data[0], data[1], data[2], data[3], data[4]);
                Platform.runLater(() -> {
                    leftData.add(dataObject);
                });
                saveData(dataObject);
            }
        }).start();
    }

    public void saveData(DataObject dataObject) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Extract the digits after the last dot in the IP address
                Pattern pattern = Pattern.compile("\\d+$");
                Matcher matcher = pattern.matcher(dataObject.ip);
                matcher.find();
                String digitsAfterLastDot = matcher.group();

                String filename = dataObject.first_name.substring(0, 3) + dataObject.last_name.substring(0, 3) + digitsAfterLastDot + ".csv";
                String directory = "processed";
                File file = new File(directory, filename);

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(dataObject.toString());
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Platform.runLater(() -> {
                    leftData.remove(dataObject);
                    readData.add(dataObject);
                });
            }
        }).start();
    }

    public void clearData() {
        ObservableList<DataObject> copy = FXCollections.observableArrayList(readData);

        for (DataObject dataObject : copy) {
            String digitsAfterLastDot = getIPDigit(dataObject.ip);
            String filename = dataObject.first_name.substring(0, 3) + dataObject.last_name.substring(0, 3) + digitsAfterLastDot + ".csv";
            String directory = "processed";

            File file = new File(directory, filename);
            file.delete();

            Platform.runLater(() -> {
                readData.remove(dataObject);
                leftData.add(dataObject);
            });
        }
    }

    public void removeElement(DataObject dataObject) {
        String digitsAfterLastDot = getIPDigit(dataObject.ip);
        String filename = dataObject.first_name.substring(0, 3) + dataObject.last_name.substring(0, 3) + digitsAfterLastDot + ".csv";
        String directory = "processed";

        File file = new File(directory, filename);
        file.delete();

        Platform.runLater(() -> {
            readData.remove(dataObject);
            leftData.add(dataObject);
        });
    }

    public String getIPDigit(String ip) {
        Pattern pattern = Pattern.compile("\\d+$");
        Matcher matcher = pattern.matcher(ip);
        matcher.find();
        return matcher.group();
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }
}
