
package com.example.projectfinalmuslih;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREF_NAME = "SportAppPref";
    private static final String KEY_LEAGUE_ID = "last_league_id";
    private static final String KEY_DARK_MODE = "dark_mode";

    private final SharedPreferences prefs;

    public SharedPrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLastLeagueId(int leagueId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_LEAGUE_ID, leagueId);
        editor.apply();
    }

    public int getLastLeagueId() {
        return prefs.getInt(KEY_LEAGUE_ID, -1);
    }

    public void setDarkMode(boolean isDark) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_DARK_MODE, isDark);
        editor.apply();
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }
}
