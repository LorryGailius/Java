package com.example.lab003;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;

public abstract class Group {
    public ObservableList<Person> personList;
    public String groupName;
    boolean isFiltered = false;

    abstract void filter(int from, int to);

    abstract void clearFilter();

    abstract void displayInfo(Tab tab);
}
