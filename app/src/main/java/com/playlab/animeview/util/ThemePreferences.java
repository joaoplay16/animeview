package com.playlab.animeview.util;

import static android.content.Context.MODE_PRIVATE;

import static com.playlab.animeview.util.ThemeUtil.getSystemTheme;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemePreferences {
    public static final String THEME_PREFERENCE_KEY = "theme_preference";
    public static final int THEME_LIGHT = AppCompatDelegate.MODE_NIGHT_NO;
    public static final int THEME_DARK = AppCompatDelegate.MODE_NIGHT_YES;

    public static void saveThemePreference(Activity activity, int themePreference) {
        SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ThemePreferences.THEME_PREFERENCE_KEY, themePreference);
        editor.apply();
    }

    public static int getSavedTheme(Activity activity){
        SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);

        return preferences.getInt(
                ThemePreferences.THEME_PREFERENCE_KEY, getSystemTheme(activity.getApplicationContext())
        );
    }
}