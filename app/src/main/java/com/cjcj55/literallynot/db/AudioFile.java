package com.cjcj55.literallynot.db;

public class AudioFile {
    private int id;
    private int userId;
    private String filePath;

    public AudioFile(int id, int userId, String filePath) {
        this.id = id;
        this.userId = userId;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFilePath() {
        return filePath;
    }
}

