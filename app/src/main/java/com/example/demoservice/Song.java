package com.example.demoservice;

import java.io.Serializable;

public class Song implements Serializable  {
    int file;
    String name;

    public Song(int file, String name) {
        this.file = file;
        this.name = name;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
