package com.example.jojo.obsido.utils;

import android.content.res.TypedArray;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.content.res.Resources.Theme;

import com.example.jojo.obsido.R;

// Util class for manipulating Shared Preferences data - methods and variables
public class SharedPreferenceUtils {

    private static final String LOG_TAG = "SharedPreferenceUtils".getClass().getSimpleName();
    /**
     * This method gets the colorPrimary color attribute from the application theme.
     * @return (int) Application colorPrimary color.
     */
    public static int getColorPrimary(Resources.Theme theme) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * This method gets the colorPrimaryDark color attrribute from the application theme.
     * @return (int) Application theme colorPrimaryDark color.
     */
    public static int getColorPrimaryDark(Resources.Theme theme) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /**
     * This method gets the colorAccent color attribute from the application theme.
     * @return (int) Application theme color primary accent.
     */
    public static int getColorAccent(Resources.Theme theme) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    public static void restartApplication(Context context) {

    }

}
