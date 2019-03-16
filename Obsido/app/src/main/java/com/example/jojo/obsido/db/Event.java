package com.example.jojo.obsido.db;

import com.example.jojo.obsido.DateTypeConverter;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters({DateTypeConverter.class})
    @ColumnInfo(name = "event_date")
    @NonNull
    private Date date;

    @ColumnInfo(name = "event_position")
    private int positions;

    @ColumnInfo(name = "event_rating")
    private int rating;

    @ColumnInfo(name = "event_comment")
    private int comment;

    public Event(Date date, int positions, int rating, int comment) {
        this.date = date;
        this.positions = positions;
        this.rating = rating;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPositions() {
        return positions;
    }

    public void setPositions(int positions) {
        this.positions = positions;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
