package com.cjcj55.literallynot.db;

import androidx.annotation.NonNull;

public class LBEntry {
    private String username;
    private int numFiles;

    public LBEntry(String username, int numFiles) {
        this.username = username;
        this.numFiles = numFiles;
    }

    @NonNull
    @Override
    public String toString() {
        return this.username + ": " + this.numFiles;
    }

    public int getNumFiles(){
        return this.numFiles;
    }

    public String getName(){
        return this.username;
    }

}