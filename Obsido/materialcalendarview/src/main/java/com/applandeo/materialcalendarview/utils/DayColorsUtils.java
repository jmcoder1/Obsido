package com.applandeo.materialcalendarview.utils;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.applandeo.materialcalendarview.R;

import android.view.View;

import java.util.Calendar;

public class DayColorsUtils {

    private DayColorsUtils() {}

    /**
     * This is general method which sets a color of the text, font type and a background of a
     * TextView object. It is used to set day cell (numbers) style.
     *
     * @param dayLabel   TextView containing a day number
     * @param textColor  A resource of a color of the day number
     * @param typeface   A type of text style, can be set as NORMAL or BOLD
     * @param background A resource of a background drawable
     */
    public static void setDayColors(TextView dayLabel, int textColor, int typeface, int background) {
        if (dayLabel == null) {
            return;
        }

        View parentView = (View) dayLabel.getParent();

        if(parentView == null) {
            return;
        }

        dayLabel.setTypeface(null, typeface);
        dayLabel.setTextColor(textColor);
        parentView.setBackgroundResource(background);

    }

    /**
     * This method sets a color of the text, font type and a background of a TextView object.
     * It is used to set day cell (numbers) style in the case of selected day (when calendar is in
     * the picker mode). It also colors a background of the selection.
     *
     * @param dayLabel           TextView containing a day number
     * @param calendarProperties A resource of a selection background color
     */
    public static void setSelectedDayColors(TextView dayLabel,
                                            CalendarProperties calendarProperties) {
        setDayColors(dayLabel, calendarProperties.getSelectedDayColor(), Typeface.NORMAL,
                R.drawable.selected_day_bg);
        View parentView = (View) dayLabel.getParent();

        ImageUtils.setDrawableBackgroundColor(parentView.getBackground(), calendarProperties.getSelectedDayColor());
    }

    /**
     * This method sets the color of the selected day background drawable.
     *
     * @param res                CalendarView resources.
     * @param calendarProperties A resource containing the calendar properties.
     */
    public static void setSelectedDayColors(Resources res, CalendarProperties calendarProperties) {
        int color = calendarProperties.getSelectedDayColor();
        if(color == 0) {
            return;
        }

        Drawable draw = res.getDrawable(R.drawable.selected_day_bg_shape);
        ImageUtils.setDrawableBackgroundColor(draw, color);
    }

    /**
     * This method sets the event day color of the selector background drawable.
     * @param res
     * @param calendarProperties
     */
    public static void setEventDayColor(Resources res, CalendarProperties calendarProperties) {
        int color = calendarProperties.getEventDayColor();
        if(color == 0) {
            return;
        }
        Drawable draw = res.getDrawable(R.drawable.selected_event_day_bg_shape);
        ImageUtils.setDrawableBackgroundColor(draw, color);
    }


    /**
     * This method sets the today day color of the selector background drawable.
     * @param res
     * @param calendarProperties
     */
    public static void setTodayDayColor(Resources res, CalendarProperties calendarProperties) {
        int color = calendarProperties.getTodayDayColor();
        if(color == 0) {
            return;
        }

        Drawable draw = res.getDrawable(R.drawable.selected_today_day_bg_shape);
        ImageUtils.setDrawableBackgroundColor(draw, color);
    }

    /**
     * This method is used to set a color of texts, font types and backgrounds of TextView objects
     * in a current visible month. Visible day labels from previous and forward months are set using
     * setDayColors() method. It also checks if a day number is a day number of today and set it
     * a different color and bold face type.
     *
     * @param day                A calendar instance representing day date
     * @param today              A calendar instance representing today date
     * @param dayLabel           TextView containing a day number
     * @param calendarProperties A resource of a color used to mark today day
     */
    public static void setCurrentMonthDayColors(Calendar day, Calendar today, TextView dayLabel,
                                                CalendarProperties calendarProperties) {
        if (today.equals(day)) {
            setDayColors(dayLabel, calendarProperties.getTodayDayColor(), Typeface.BOLD,
                    R.drawable.selected_today_day_bg);
        } else if(calendarProperties.getEventCalendarDays().contains(day)) {
            setDayColors(dayLabel, calendarProperties.getEventDayColor(), Typeface.BOLD,
                    R.drawable.selected_event_day_bg);
        } else {
            setDayColors(dayLabel, calendarProperties.getDaysLabelsColor(), Typeface.NORMAL,
                    R.drawable.selected_day_bg);
        }
    }


    /**
     * This method is used to set the background color of the day background resource objects.
     * @param res
     * @param calendarProperties A resource of a color used to change colors.
     */
    public static void setDaysBackgroundColor(Resources res, CalendarProperties calendarProperties) {
        Drawable todayDayDraw = res.getDrawable(R.drawable.selected_today_day_bg_shape);
        Drawable eventDayDraw = res.getDrawable(R.drawable.selected_event_day_bg_shape);
        Drawable selectedDayDraw = res.getDrawable(R.drawable.selected_day_bg_shape);


        if(calendarProperties.getTodayDayColor() != 0) {
            ImageUtils.setDrawableBackgroundColor(todayDayDraw, calendarProperties.getTodayDayColor());
        }


        if(calendarProperties.getEventDayColor() != 0) {
            ImageUtils.setDrawableBackgroundColor(eventDayDraw, calendarProperties.getEventDayColor());
        }

        if(calendarProperties.getSelectedDayColor() != 0) {
            ImageUtils.setDrawableBackgroundColor(selectedDayDraw, calendarProperties.getSelectedDayColor());
        }

    }


}
