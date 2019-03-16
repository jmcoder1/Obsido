package com.example.jojo.obsido.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.jojo.obsido.db.EventDatabase;
import com.example.jojo.obsido.db.Event;
import com.example.jojo.obsido.db.dao.EventDao;

import java.util.List;

import androidx.lifecycle.LiveData;

public class EventRepository {

    private EventDao eventDao;
    private LiveData<List<Event>> allEvents;

    public EventRepository(Application application) {
        EventDatabase db = EventDatabase.getInstance(application);
        eventDao = db.eventDao();
        allEvents = eventDao.getAllEvents();
    }

    public void insert(Event event) {
        new InsertEventAsyncTask(eventDao).execute(event);
    }

    public void update(Event event) {
        new UpdateEventAsyncTask(eventDao).execute(event);
    }

    public void delete(Event event) {
        new DeleteEventAsyncTask(eventDao).execute(event);
    }

    public void deleteAllEvents() {
        new DeleteAllEventAsyncTask(eventDao).execute();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    private static class InsertEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao eventDao;

        private InsertEventAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        protected Void doInBackground(Event... events) {
            eventDao.insert(events[0]);
            return null;
        }
    }

    private static class UpdateEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao eventDao;

        private UpdateEventAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        protected Void doInBackground(Event... events) {
            eventDao.update(events[0]);
            return null;
        }
    }

    private static class DeleteEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao eventDao;

        private DeleteEventAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        protected Void doInBackground(Event... events) {
            eventDao.delete(events[0]);
            return null;
        }
    }

    private static class DeleteAllEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao eventDao;

        private DeleteAllEventAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        protected Void doInBackground(Event... events) {
            eventDao.deleteAllEvents();
            return null;
        }
    }
}
