package com.example.lab004;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;

public abstract class Group {
    public ObservableList<Person> personList;
    public String groupName;
    int lastIdx = 0;
    boolean isFiltered = false;

    abstract void filter(int from, int to);

    abstract void clearFilter();

    abstract void displayInfo(Tab tab);
}
