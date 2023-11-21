package com.playlab.animeview.util;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.UiModeManager;
import android.content.Context;

public class ThemeUtil {
    public static int getSystemTheme(Context context){
        UiModeManager uiModeManager = getSystemService(context, UiModeManager.class);
        if (uiModeManager != null) {
            return uiModeManager.getNightMode();
        }
        return -1;
    }
}
