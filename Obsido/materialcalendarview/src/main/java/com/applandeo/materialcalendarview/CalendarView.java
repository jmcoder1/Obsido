package com.applandeo.materialcalendarview;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.adapters.CalendarPageAdapter;
import com.applandeo.materialcalendarview.exceptions.ErrorsMessages;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.extensions.CalendarViewPager;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.utils.AppearanceUtils;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.applandeo.materialcalendarview.utils.DayColorsUtils;
import com.applandeo.materialcalendarview.utils.ImageUtils;
import com.applandeo.materialcalendarview.utils.SelectedDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.applandeo.materialcalendarview.utils.CalendarProperties.FIRST_VISIBLE_PAGE;

/**
 * This class represents a view, displays to user as calendar. It allows to work in date picker
 * mode or like a normal calendar. In a normal calendar mode it can displays an image under the day
 * number. In both modes it marks today day. It also provides click on day events using
 * OnDayClickListener which returns an EventDay object.
 *
 * @see EventDay
 * @see OnDayClickListener

 * XML attributes:
 * - Set selection color: selectionColor="@color/[color]"
 * - Set today color: todayColor="@color/[color]"
 * - Set event day color: eventDayColor="@color/[color]"
 * - Set toolbar color: toolbarColor="@color/[color]"
 * - Set week day bar color: weekDayBarColor="@color/[color]"
 */

public class CalendarView extends LinearLayout {

    public static final int CLASSIC = 0;
    public static final int ONE_DAY_PICKER = 1;
    public static final int MANY_DAYS_PICKER = 2;
    public static final int RANGE_PICKER = 3;

    private Context mContext;
    private CalendarPageAdapter mCalendarPageAdapter;

    private String mCalendarTitleDate;
    private int mCurrentPage;
    private CalendarViewPager mViewPager;

    private CalendarProperties mCalendarProperties;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
        initCalendar();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
        initCalendar();
    }

    // protected constructor to create CalendarView for the dialog date picker
    protected CalendarView(Context context, CalendarProperties calendarProperties) {
        super(context);
        mContext = context;
        mCalendarProperties = calendarProperties;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view, this);

        mViewPager = findViewById(R.id.calendarViewPager);

        initAttributes();
        initCalendar();
    }

    private void initControl(Context context, AttributeSet attrs) {
        mContext = context;
        mCalendarProperties = new CalendarProperties(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view, this);

        mViewPager = findViewById(R.id.calendarViewPager);

        setAttributes(attrs);
    }

    /**
     * This method set xml values for calendar elements
     *
     * @param attrs A set of xml attributes
     */
    private void setAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            initCalendarProperties(typedArray);
            initAttributes();
        } finally {
            typedArray.recycle();
        }
    }

    private void initCalendarProperties(TypedArray typedArray) {
        int weekDayBarColor = typedArray.getColor(R.styleable.CalendarView_weekDayBarColor, 0);
        mCalendarProperties.setWeekDayBarColor(weekDayBarColor);

        int weekDayLabelColor = typedArray.getColor(R.styleable.CalendarView_weekDayLabelColor, 0);
        mCalendarProperties.setWeekDayLabelColor(weekDayLabelColor);

        int pagesColor = typedArray.getColor(R.styleable.CalendarView_pagesColor, 0);
        mCalendarProperties.setPagesColor(pagesColor);

        int daysLabelsColor = typedArray.getColor(R.styleable.CalendarView_daysLabelsColor, 0);
        mCalendarProperties.setDaysLabelsColor(daysLabelsColor);

        int anotherMonthsDaysLabelsColor = typedArray.getColor(R.styleable.CalendarView_anotherMonthsDaysLabelsColor, 0);
        mCalendarProperties.setAnotherMonthsDaysLabelsColor(anotherMonthsDaysLabelsColor);

        int selectedDayColor = typedArray.getColor(R.styleable.CalendarView_selectedDayColor, 0);
        mCalendarProperties.setSelectedDayColor(selectedDayColor);

        int todayDayColor = typedArray.getColor(R.styleable.CalendarView_todayDayColor, 0);
        mCalendarProperties.setTodayDayColor(todayDayColor);

        int eventDayColor = typedArray.getColor(R.styleable.CalendarView_eventDayColor, 0);
        mCalendarProperties.setEventDayColor(eventDayColor);

        int eventIconColor = typedArray.getColor(R.styleable.CalendarView_eventIconColor, 0);
        mCalendarProperties.setEventIconColor(eventIconColor);

        int disabledDaysLabelsColor = typedArray.getColor(R.styleable.CalendarView_disabledDaysLabelsColor, 0);
        mCalendarProperties.setDisabledDaysLabelsColor(disabledDaysLabelsColor);

        int calendarType = typedArray.getInt(R.styleable.CalendarView_type, CLASSIC);
        mCalendarProperties.setCalendarType(calendarType);

        boolean eventsEnabled = typedArray.getBoolean(R.styleable.CalendarView_eventsEnabled,
                mCalendarProperties.getCalendarType() == CLASSIC);
        mCalendarProperties.setEventsEnabled(eventsEnabled);

        boolean swipeEnabled = typedArray.getBoolean(R.styleable.CalendarView_swipeEnabled, true);
        mCalendarProperties.setSwipeEnabled(swipeEnabled);

    }

    private void initAttributes() {
        AppearanceUtils.setWeekDayBarColor(getRootView(), mCalendarProperties.getWeekDayBarColor());

        AppearanceUtils.setWeekDayLabels(getRootView(), mCalendarProperties.getWeekDayLabelColor(),
                mCalendarProperties.getFirstPageCalendarDate().getFirstDayOfWeek());

        AppearanceUtils.setPagesColor(getRootView(), mCalendarProperties.getPagesColor());

        mViewPager.setSwipeEnabled(mCalendarProperties.getSwipeEnabled());

        AppearanceUtils.setPagesColor(getRootView(), mCalendarProperties.getPagesColor());

        DayColorsUtils.setDaysBackgroundColor(getResources(), mCalendarProperties);

        ImageUtils.setEventIconColor(mCalendarProperties);

        // Sets layout for date picker or normal calendar
        setCalendarRowLayout();
    }

    private void setCalendarRowLayout() {
        if (mCalendarProperties.getEventsEnabled()) {
            mCalendarProperties.setItemLayoutResource(R.layout.calendar_view_day);
        } else {
            mCalendarProperties.setItemLayoutResource(R.layout.calendar_view_picker_day);
        }
    }

    private void initCalendar() {
        mCalendarPageAdapter = new CalendarPageAdapter(mContext, mCalendarProperties);

        mViewPager.setAdapter(mCalendarPageAdapter);
        mViewPager.addOnPageChangeListener(onPageChangeListener);

        setUpCalendarPosition(Calendar.getInstance());
    }

    private void setUpCalendarPosition(Calendar calendar) {
        DateUtils.setMidnight(calendar);
        mCalendarTitleDate = DateUtils.getMonthAndYearDate(mContext, calendar);

        if (mCalendarProperties.getCalendarType() == CalendarView.ONE_DAY_PICKER) {
            mCalendarProperties.setSelectedDay(calendar);
        }

        mCalendarProperties.getFirstPageCalendarDate().setTime(calendar.getTime());
        mCalendarProperties.getFirstPageCalendarDate().add(Calendar.MONTH, -FIRST_VISIBLE_PAGE);

        mViewPager.setCurrentItem(FIRST_VISIBLE_PAGE);
    }

    public void setOnPreviousPageChangeListener(OnCalendarPageChangeListener listener) {
        mCalendarProperties.setOnPreviousPageChangeListener(listener);
    }

    public void setOnForwardPageChangeListener(OnCalendarPageChangeListener listener) {
        mCalendarProperties.setOnForwardPageChangeListener(listener);
    }

    private final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        /**
         * This method set calendar header label
         *
         * @param position Current ViewPager position
         * @see ViewPager.OnPageChangeListener
         */
        @Override
        public void onPageSelected(int position) {
            Calendar calendar = (Calendar) mCalendarProperties.getFirstPageCalendarDate().clone();
            calendar.add(Calendar.MONTH, position);

            if (!isScrollingLimited(calendar, position)) {
                setHeaderName(calendar, position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private boolean isScrollingLimited(Calendar calendar, int position) {
        if (DateUtils.isMonthBefore(mCalendarProperties.getMinimumDate(), calendar)) {
            mViewPager.setCurrentItem(position + 1);
            return true;
        }

        if (DateUtils.isMonthAfter(mCalendarProperties.getMaximumDate(), calendar)) {
            mViewPager.setCurrentItem(position - 1);
            return true;
        }

        return false;
    }

    private void setHeaderName(Calendar calendar, int position) {
        mCalendarTitleDate = DateUtils.getMonthAndYearDate(mContext, calendar);
        callOnPageChangeListeners(position);
    }

    public String getCalendarTitleDate() {
        return mCalendarTitleDate;
    }

    // This method calls page change listeners after swipe calendar or click arrow buttons
    private void callOnPageChangeListeners(int position) {
        if (position > mCurrentPage && mCalendarProperties.getOnForwardPageChangeListener() != null) {
            mCalendarProperties.getOnForwardPageChangeListener().onChange();
        }

        if (position < mCurrentPage && mCalendarProperties.getOnPreviousPageChangeListener() != null) {
            mCalendarProperties.getOnPreviousPageChangeListener().onChange();
        }

        mCurrentPage = position;
    }

    /**
     * @param onDayClickListener OnDayClickListener interface responsible for handle clicks on calendar cells
     * @see OnDayClickListener
     */
    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        mCalendarProperties.setOnDayClickListener(onDayClickListener);
    }

    /**
     * This method set a current and selected date of the calendar using Calendar object.
     *
     * @param date A Calendar object representing a date to which the calendar will be set
     */
    public void setDate(Calendar date) throws OutOfDateRangeException {
        if (mCalendarProperties.getMinimumDate() != null && date.before(mCalendarProperties.getMinimumDate())) {
            throw new OutOfDateRangeException(ErrorsMessages.OUT_OF_RANGE_MIN);
        }

        if (mCalendarProperties.getMaximumDate() != null && date.after(mCalendarProperties.getMaximumDate())) {
            throw new OutOfDateRangeException(ErrorsMessages.OUT_OF_RANGE_MAX);
        }

        setUpCalendarPosition(date);
        mCalendarTitleDate = DateUtils.getMonthAndYearDate(mContext, date);

        mCalendarPageAdapter.notifyDataSetChanged();
    }

    /**
     * This method set a current and selected date of the calendar using Date object.
     *
     * @param currentDate A date to which the calendar will be set
     */
    public void setDate(Date currentDate) throws OutOfDateRangeException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        setDate(calendar);
    }

    public void hideEvents() {
        List<ImageView> icons = mCalendarProperties.getEventDayIcons();
        mCalendarProperties.setShowEventIcons(false);
        for(int i = 0; i < icons.size(); i++) {
            ImageView icon = icons.get(i);
            icon.setVisibility(INVISIBLE);
        }
    }

    public void showEvents() {
        List<ImageView> icons = mCalendarProperties.getEventDayIcons();
        mCalendarProperties.setShowEventIcons(true);
        for(int i = 0; i < icons.size(); i++) {
            ImageView icon = icons.get(i);
            icon.setVisibility(VISIBLE);
        }
    }

    public void setEventDays(List<EventDay> eventDays) {
        if (mCalendarProperties.getEventsEnabled() && eventDays != null) {
            mCalendarProperties.setEventDays(eventDays);
            mCalendarProperties.setEventCalendarDays(initCalendarList(eventDays));
            mCalendarPageAdapter.notifyDataSetChanged();
            ImageUtils.setEventIconColor(mCalendarProperties);
        }
    }

    public List<EventDay> getEventDays() {
        return mCalendarProperties.getEventDays();
    }

    private List<Calendar> initCalendarList(List<EventDay> eventDays) {
        List<Calendar> calendarDays = new ArrayList<>();
        for(int i = 0; i < eventDays.size(); i++) {
            calendarDays.add(eventDays.get(i).getCalendar());
        }

        return calendarDays;
    }

    public List<Calendar> getSelectedDates() {
        return Stream.of(mCalendarPageAdapter.getSelectedDays())
                .map(SelectedDay::getCalendar)
                .sortBy(calendar -> calendar).toList();
    }

    public void setSelectedDates(List<Calendar> selectedDates) {
        mCalendarProperties.setSelectedDays(selectedDates);
    }

    @Deprecated
    public Calendar getSelectedDate() {
        return getFirstSelectedDate();
    }

    public Calendar getFirstSelectedDate() {
        return Stream.of(mCalendarPageAdapter.getSelectedDays())
                .map(SelectedDay::getCalendar).findFirst().get();
    }

    public Calendar getCurrentPageDate() {
        Calendar calendar = (Calendar) mCalendarProperties.getFirstPageCalendarDate().clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, mViewPager.getCurrentItem());
        return calendar;
    }

    public void setMinimumDate(Calendar calendar) {
        mCalendarProperties.setMinimumDate(calendar);
    }

    public void setMaximumDate(Calendar calendar) {
        mCalendarProperties.setMaximumDate(calendar);
    }

    public void showCurrentMonthPage() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()
                - DateUtils.getMonthsBetweenDates(DateUtils.getCalendar(), getCurrentPageDate()), true);
    }

    public void setDisabledDays(List<Calendar> disabledDays) {
        mCalendarProperties.setDisabledDays(disabledDays);
    }

    public void setSelectionColor(int color) {
        mCalendarProperties.setSelectedDayColor(color);
        DayColorsUtils.setSelectedDayColors(getResources(), mCalendarProperties);
    }

    public int getSelectionColor() {
        return mCalendarProperties.getSelectedDayColor();
    }

    public void setTodayColor(int color) {
        mCalendarProperties.setTodayDayColor(color);
        DayColorsUtils.setTodayDayColor(getResources(), mCalendarProperties);
    }

    public int getTodayDayColor() {
        return mCalendarProperties.getTodayDayColor();
    }

    public void setEventDayColor(int color) {
        mCalendarProperties.setEventDayColor(color);
        DayColorsUtils.setEventDayColor(getResources(), mCalendarProperties);
    }

    public int getEventDayColor() {
        return mCalendarProperties.getEventDayColor();
    }

    public void setEventIconColor(int color) {
        mCalendarProperties.setEventIconColor(color);
        ImageUtils.setEventIconColor(mCalendarProperties);
    }

    public int getEventIconColor() { return mCalendarProperties.getEventIconColor(); }

    public void setWeekDayBarColor(int color) {
        mCalendarProperties.setWeekDayBarColor(color);
        AppearanceUtils.setWeekDayBarColor(getRootView(), color);
    }

    public int getWeekDayBarColor() {
        return mCalendarProperties.getWeekDayBarColor();
    }

}
