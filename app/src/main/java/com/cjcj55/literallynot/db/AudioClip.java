package com.cjcj55.literallynot.db;

public class AudioClip {
    private final String filePath;
    private final String title;
    private final String artist;
    private final String album;
    private final String timeSaid;

    public AudioClip(String filePath, String title, String artist, String album, String timeSaid) {
        this.filePath = filePath;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.timeSaid = timeSaid;
    }

    public AudioClip(String filePath, String timeSaid) {
        this.filePath = filePath;
        this.timeSaid = timeSaid;
        this.title = "Literally?!";
        this.artist = "you!";
        this.album = "Literally detox";
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTimeSaid() {
        return timeSaid;
    }
}

