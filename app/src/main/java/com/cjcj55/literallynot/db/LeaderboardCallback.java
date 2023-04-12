package com.cjcj55.literallynot.db;

import java.util.List;

public interface LeaderboardCallback {
    void onSuccess(List<LBEntry> leaderboard);
    void onFailure();
}
