package com.cjcj55.literallynot.db;


import java.util.List;

public interface WeekDataCallback {
    void onWeekDataReceived(List<String> datetimeList);
}
