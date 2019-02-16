package com.applandeo.materialcalendarview.utils;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

public class ImageUtils {

    private ImageUtils() { }

    public static void loadImage(ImageView imageView, Object image, CalendarProperties calendarProperties) {
        if (image == null) {
            return;
        }

        Drawable drawable = null;
        if (image instanceof Drawable) {
            drawable = (Drawable) image;
        } else if (image instanceof Integer) {
            drawable = ContextCompat.getDrawable(imageView.getContext(), (Integer) image);
        }

        if (drawable == null) {
            return;
        }

        // Sets the image color
        if(calendarProperties.getEventIconColor() != 0) {
            setDrawableBackgroundColor(drawable, calendarProperties.getEventIconColor());
        }

        if(calendarProperties.getShowEventIcons()) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }

        imageView.setImageDrawable(drawable);
    }


    /**
     * This method sets the background oolor for a drawable.
     * @param draw The drawable background.
     * @param color The color.
     */
    public static void setDrawableBackgroundColor(@NonNull Drawable draw, int color) {
        if(color == 0) {
            return;
        }

        if(Build.VERSION.SDK_INT < 21) {
            DrawableCompat.setTint(draw, color);
        } else {
            draw.setTint(color);
        }
    }

    /**
     * This mehod sets the color of the event day icon.
     * @param calendarProperties The resource class containing the color attributes.
     */
    public static void setEventIconColor(CalendarProperties calendarProperties) {
        List<ImageView> icons = calendarProperties.getEventDayIcons();
        int eventIconColor = calendarProperties.getEventIconColor();
        for(int i = 0; i < icons.size(); i++) {
            ImageView icon = icons.get(i);
            icon.setColorFilter(eventIconColor);
        }
    }
}
