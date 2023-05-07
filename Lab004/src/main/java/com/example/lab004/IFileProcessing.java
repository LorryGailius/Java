package com.example.lab004;

import java.io.File;

public interface IFileProcessing {
    public void readFromFile(File file);
    public void writeToFile(File file);
    public void saveToPDF(File file);
}
