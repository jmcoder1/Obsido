package com.example.jojo.obsido.utils;

import android.content.res.Resources;
import android.content.res.TypedArray;
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

    public static String getEventActString(Event event, Resources res) {
        String eventActString = "";
        if(event.isSex()) {
            eventActString = res.getString(R.string.event_act_sex);
        }

        if(event.isHandjob()) {
            eventActString = res.getString(R.string.event_act_hj);
        }

        if(event.isBlowjob()) {
            eventActString = res.getString(R.string.event_act_bj);
        }

        if(event.isAnal()) {
            eventActString = res.getString(R.string.event_act_anal);
        }
        return eventActString;
    }
}
