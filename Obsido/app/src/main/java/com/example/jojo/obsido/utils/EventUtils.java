package com.example.jojo.obsido.utils;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.example.jojo.obsido.R;
import com.example.jojo.obsido.db.Event;


public class EventUtils {

    public static Drawable getEventDrawable(Event event, Resources res) {
        TypedArray eventActsImg = res.obtainTypedArray(R.array.event_acts_img);

        if(event.isSex()) {
            return eventActsImg.getDrawable(Event.EVENT_SEX_INDEX);
        }

        if(event.isHandjob()) {
            return eventActsImg.getDrawable(Event.EVENT_HANDJOB_INDEX);
        }

        if(event.isBlowjob()) {
            return eventActsImg.getDrawable(Event.EVENT_BLOWJOB_INDEX);
        }

        if(event.isAnal()) {
            return eventActsImg.getDrawable(Event.EVENT_ANAL_INDEX);
        }
        eventActsImg.recycle();
        // Default image is all else fails
        return res.getDrawable(R.drawable.test_event);
    }
}
