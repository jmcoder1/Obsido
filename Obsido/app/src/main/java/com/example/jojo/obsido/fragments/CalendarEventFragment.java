package com.example.jojo.obsido.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jojo.obsido.CalendarEventAdapter;
import com.example.jojo.obsido.R;
import com.example.jojo.obsido.db.Event;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarEventFragment extends Fragment {

    private RecyclerView calendarEventRecyclerView;

    private List<Event> mEvents;
    private CalendarEventAdapter calendarEventAdapter;

    public CalendarEventFragment() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootViewCalendarEvents = inflater.inflate(R.layout.fragment_calendar_event_list, container, false);
        calendarEventRecyclerView = rootViewCalendarEvents.findViewById(R.id.calendar_events_recycler_view);

        initRecyclerView();

        return rootViewCalendarEvents;
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
        calendarEventAdapter.submitList(mEvents);

    }

    private void initRecyclerView() {
        calendarEventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        calendarEventRecyclerView.setLayoutManager(llm);
        calendarEventRecyclerView.setHasFixedSize(true);

        calendarEventAdapter = new CalendarEventAdapter();
        calendarEventRecyclerView.setAdapter(calendarEventAdapter);

        calendarEventAdapter.submitList(mEvents);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // TODO: do this with callback partnerViewModel.delete(calendarEventAdapter.getCalendarEventAt(
                        //viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(calendarEventRecyclerView);

    }
}
