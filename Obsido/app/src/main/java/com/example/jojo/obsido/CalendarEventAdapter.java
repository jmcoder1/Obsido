package com.example.jojo.obsido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jojo.obsido.db.Event;
import com.example.jojo.obsido.utils.EventUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarEventAdapter extends ListAdapter<Event, CalendarEventAdapter.CalendarEventHolder> {
    private OnItemClickListener listener;
    public CalendarEventAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Event>() {
        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getComment().equals(newItem.getComment()) &&
                    oldItem.isAnal() == newItem.isAnal() &&
                    oldItem.isBlowjob() == newItem.isBlowjob() &&
                    oldItem.isSex() == newItem.isSex() &&
                    oldItem.isHandjob() == newItem.isHandjob();
        }
    };

    @NonNull
    @Override
    public CalendarEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_event_item, parent, false);
        return new CalendarEventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarEventHolder holder, int position) {
        final SimpleDateFormat stringDateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.UK);

        Event currentCalendarEvent = getItem(position);
        String calendarComment = currentCalendarEvent.getComment();
        String calendarDateString = stringDateFormat.format(currentCalendarEvent.getDate());


        holder.calendarEventCommentTextView.setText(calendarComment);
        holder.calendarEventDateTextView.setText(calendarDateString);
        holder.calendarEventActTextView.setText(EventUtils.getEventActString
                (currentCalendarEvent, holder.itemView.getContext().getResources()));
        holder.calendarEventIcon.setImageDrawable(EventUtils.getEventDrawable
                (currentCalendarEvent, holder.itemView.getContext().getResources()));

    }

    public Event getCalendarEventAt(int position) {
        return getItem(position);
    }

    class CalendarEventHolder extends RecyclerView.ViewHolder {
        private TextView calendarEventDateTextView;
        private TextView calendarEventActTextView;
        private TextView calendarEventCommentTextView;
        private ImageView calendarEventIcon;

        public CalendarEventHolder(View itemView) {
            super(itemView);
            calendarEventDateTextView = itemView.findViewById(R.id.calendar_event_date);
            calendarEventActTextView = itemView.findViewById(R.id.calendar_event_act);
            calendarEventCommentTextView = itemView.findViewById(R.id.calendar_event_comment);
            calendarEventIcon = itemView.findViewById(R.id.calendar_event_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
