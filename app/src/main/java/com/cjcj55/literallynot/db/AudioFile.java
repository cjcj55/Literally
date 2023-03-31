package com.cjcj55.literallynot.db;

public class AudioFile {
    private String fileName;
    private String timeSaid;
    private byte[] audioData;

    public AudioFile(String fileName, String timeSaid, byte[] audioData) {
        this.fileName = fileName;
        this.timeSaid = timeSaid;
        this.audioData = audioData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTimeSaid() {
        return timeSaid;
    }

    public void setTimeSaid(String timeSaid) {
        this.timeSaid = timeSaid;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public void setAudioData(byte[] audioData) {
        this.audioData = audioData;
    }
}
