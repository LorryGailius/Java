package com.example.lab004;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindowController {

    List<StudentGroup> groups = new ArrayList<StudentGroup>();
    Student editableStudent = null;
    int group_id = 0;

    @FXML
    private TextField StudentNameField;
    @FXML
    private TextField AttendanceField;
    @FXML
    private TextField FilterFromField;
    @FXML
    private TextField FilterToField;
    @FXML
    private TabPane TabPane;

    @FXML
    public void create_tab() {
        Tab new_tab = new Tab("Group " + (group_id + 1));
        TabPane.getTabs().add(new_tab);

        StudentGroup new_group = new StudentGroup();
        new_group.group_id = groups.size() + 1;
        new_group.groupName = "Group " + (group_id + 1);
        groups.add(new_group);
        new_group.displayInfo(new_tab);
        group_id++;
    }

    @FXML
    public void add_student() {
        if (groups.size() == 0 || StudentNameField.getText().isEmpty()) {
            return;
        }

        Student new_student = new Student();

        int selected_tab = TabPane.getSelectionModel().getSelectedIndex();

        if (groups.get(selected_tab).isFiltered) {
            return;
        }

        new_student.id = groups.get(selected_tab).personList.size() + 1;

        new_student.name = StudentNameField.getText();

        if (AttendanceField.getText().isEmpty()) {
            new_student.attendance = null;
        } else {
            String[] attendance = AttendanceField.getText().split(" ");
            new_student.attendance = new int[attendance.length];
            for (int i = 0; i < attendance.length; i++) {
                try {
                    new_student.attendance[i] = Integer.parseInt(attendance[i]);
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                    new_student.attendance = null;
                }
            }
            new_student.setAttendanceString(new_student.attendance);
        }

        groups.get(selected_tab).personList.add(new_student);
        StudentNameField.setText("");
        AttendanceField.setText("");
    }

    @FXML
    public void edit_student() {
        if (groups.size() == 0) {
            return;
        }

        int selected_tab = TabPane.getSelectionModel().getSelectedIndex();

        if (groups.get(selected_tab).personList.size() == 0 || groups.get(selected_tab).isFiltered) {
            return;
        }

        editableStudent = (Student) groups.get(selected_tab).table.getSelectionModel().getSelectedItem();

        if (editableStudent == null) {
            return;
        }

        StudentNameField.setText(editableStudent.name);
        AttendanceField.setText(editableStudent.attendanceString);
    }

    @FXML
    public void save_student() {
        if (editableStudent == null || groups.get(TabPane.getSelectionModel().getSelectedIndex()).isFiltered) {
            return;
        }

        int idx = groups.get(TabPane.getSelectionModel().getSelectedIndex()).personList.indexOf(editableStudent);

        groups.get(TabPane.getSelectionModel().getSelectedIndex()).personList.get(idx).name = StudentNameField.getText();

        if (AttendanceField.getText().isEmpty()) {
            editableStudent.attendance = null;
        } else {
            String[] attendance = AttendanceField.getText().split(" ");
            editableStudent.attendance = new int[attendance.length];
            for (int i = 0; i < attendance.length; i++) {
                try {
                    editableStudent.attendance[i] = Integer.parseInt(attendance[i]);
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                    editableStudent.attendance = null;
                }
            }
            editableStudent.setAttendanceString(editableStudent.attendance);
        }

        groups.get(TabPane.getSelectionModel().getSelectedIndex()).table.getSelectionModel().clearSelection();
        groups.get(TabPane.getSelectionModel().getSelectedIndex()).table.refresh();

        StudentNameField.setText("");
        AttendanceField.setText("");
        editableStudent = null;
    }

    @FXML
    public void delete_student() {
        if (groups.size() == 0) {
            return;
        }

        int selected_tab = TabPane.getSelectionModel().getSelectedIndex();

        if (groups.get(selected_tab).personList.size() == 0 || groups.get(selected_tab).isFiltered) {
            return;
        }

        Student studentToDelete = (Student) groups.get(selected_tab).table.getSelectionModel().getSelectedItem();

        if (studentToDelete == null) {
            return;
        }

        groups.get(selected_tab).personList.remove(studentToDelete);
        groups.get(TabPane.getSelectionModel().getSelectedIndex()).table.refresh();
        groups.get(TabPane.getSelectionModel().getSelectedIndex()).table.getSelectionModel().clearSelection();
    }

    @FXML
    public void delete_group() {
        int selected_tab = TabPane.getSelectionModel().getSelectedIndex();

        groups.remove(selected_tab);
        TabPane.getTabs().remove(selected_tab);
    }

    @FXML
    public void filter() {
        if (groups.size() == 0) {
            return;
        }

        int selected_tab = TabPane.getSelectionModel().getSelectedIndex(), from = 0, to = 0;

        if (!FilterFromField.getText().isEmpty() && !FilterToField.getText().isEmpty()) {
            try {
                from = Integer.parseInt(FilterFromField.getText());
                to = Integer.parseInt(FilterToField.getText());
                groups.get(selected_tab).filter(from, to);
                groups.get(selected_tab).table.refresh();
            } catch (NumberFormatException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    @FXML
    public void clear_filter() {
        if (groups.size() == 0) {
            return;
        }

        int selected_tab = TabPane.getSelectionModel().getSelectedIndex();

        if (!groups.get(selected_tab).isFiltered) {
            return;
        }

        groups.get(selected_tab).clearFilter();
        groups.get(selected_tab).table.refresh();
        FilterFromField.clear();
        FilterToField.clear();
    }

    @FXML
    public void close() {
        Platform.exit();
    }

    @FXML
    public void save() {
        if(groups.size() == 0) {
            return;
        }


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);

                for(int i = 0; i < groups.size(); i++) {
                    fileWriter.write(groups.get(i).groupName + "," + groups.get(i).personList.size() + "\n");
                    for (int j = 0; j < groups.get(i).personList.size(); j++) {
                        Student student = (Student) groups.get(i).personList.get(j);
                        fileWriter.write(student.id + "," + student.name + "," + student.attendanceString + "\n");
                    }
                }
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    @FXML
    public void open() {
        groups.clear();
        TabPane.getTabs().clear();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showOpenDialog(null);

        if(file != null)
        {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 2) {
                        StudentGroup group = new StudentGroup();
                        group.groupName = data[0];
                        int size = Integer.parseInt(data[1]);

                        for (int i = 0; i < size; i++) {
                            line = bufferedReader.readLine();
                            data = line.split(",");
                            Student student = new Student();
                            student.id = Integer.parseInt(data[0]);
                            student.name = data[1];
                            student.attendanceString = data[2];
                            student.attendance = new int[student.attendanceString.split(" ").length];
                            for (int j = 0; j < student.attendance.length; j++) {
                                student.attendance[j] = Integer.parseInt(student.attendanceString.split(" ")[j]);
                            }
                            group.personList.add(student);
                        }
                        groups.add(group);
                        group_id  = Integer.parseInt(group.groupName.split(" ")[1]);
                        Tab tab = new Tab(group.groupName);
                        TabPane.getTabs().add(tab);
                        group.displayInfo(tab);
                    }
                }
            }
            catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

    }

}
