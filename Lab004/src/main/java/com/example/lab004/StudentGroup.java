package com.example.lab004;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class StudentGroup extends Group {

    int group_id = 0;
    public ObservableList<Student> personList = FXCollections.observableList(new ArrayList<Student>());
    public ArrayList<Student> originalList = new ArrayList<Student>();
    TableColumn<Student, Integer> idColumn = new TableColumn<>("ID");
    TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
    TableColumn<Student, String> attendanceColumn = new TableColumn<>("Attendance");
    TableView table;
    VBox vbox;

    @Override
    void filter(int from, int to) {

        if (!isFiltered) {
            originalList = new ArrayList<Student>();
            for (Student student : personList) {
                Student temp = student;
                originalList.add(temp);
            }
            isFiltered = true;
        }

        personList.clear();

        //Check if array size is between from and to
        for (Student student : originalList) {
            int last = student.getAttendance().length - 1;
            if (last >= from) {
                Student temp = new Student();
                temp.setId(student.getId());
                temp.setName(student.getName());
                int diff = Math.min(to - from + 1, student.getAttendance().length - from);
                int[] newAttendance = new int[diff];
                for (int i = 0; i < newAttendance.length; i++) {
                    System.out.println(i + " " + from);
                    if (i + from > last)
                        break;
                    newAttendance[i] = student.getAttendance()[i + from];
                }
                temp.setAttendance(newAttendance);
                temp.setAttendanceString(newAttendance);
                personList.add(temp);
            }
        }
    }

    @Override
    void clearFilter() {
        if (isFiltered) {
            personList.clear();
            for (Student student : originalList) {
                Student temp = student;
                temp.id = student.id;
                temp.name = student.name;
                temp.attendance = student.attendance;
                temp.attendanceString = student.attendanceString;
                personList.add(temp);
            }
            isFiltered = false;
        }
    }

    @Override
    void displayInfo(Tab tab) {
        table = new TableView<>();
        vbox = new VBox(table);
        VBox.setVgrow(table, Priority.ALWAYS);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("attendanceString"));

        idColumn.setSortable(false);
        nameColumn.setSortable(false);
        attendanceColumn.setSortable(false);

        table.getColumns().addAll(idColumn, nameColumn, attendanceColumn);
        table.setItems(personList);

        idColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
        attendanceColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.5));


        tab.setContent(vbox);
    }
}
