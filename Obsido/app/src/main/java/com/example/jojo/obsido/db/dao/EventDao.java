package com.example.jojo.obsido.db.dao;

import com.example.jojo.obsido.db.Event;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EventDao {

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM events")
    void deleteAllEvents();

    @Query("SELECT * FROM events")
    LiveData<List<Event>> getAllEvents();

}
