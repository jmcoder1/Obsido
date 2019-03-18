package com.example.jojo.obsido.db;

import com.example.jojo.obsido.utils.DateTypeConverterUtils;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "events")
public class Event {

    public static final int EVENT_SEX_INDEX = 0;
    public static final int EVENT_HANDJOB_INDEX = 1;
    public static final int EVENT_BLOWJOB_INDEX = 2;
    public static final int EVENT_ANAL_INDEX = 3;

    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters({DateTypeConverterUtils.class})
    @ColumnInfo(name = "event_date")
    @NonNull
    private Date date;

    @ColumnInfo(name = "event_comment")
    private String comment;

    @ColumnInfo(name = "sex")
    private boolean sex;

    @ColumnInfo(name = "handjob")
    private boolean handjob;

    @ColumnInfo(name = "blowjob")
    private boolean blowjob;

    @ColumnInfo(name = "anal")
    private boolean anal;

    public Event(@NonNull Date date, String comment, boolean sex, boolean handjob, boolean blowjob,
                 boolean anal) {
        this.date = date;
        this.comment = comment;
        this.sex = sex;
        this.handjob = handjob;
        this.blowjob = blowjob;
        this.anal = anal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean isHandjob() {
        return handjob;
    }

    public void setHandjob(boolean handjob) {
        this.handjob = handjob;
    }

    public boolean isBlowjob() {
        return blowjob;
    }

    public void setBlowjob(boolean blowjob) {
        this.blowjob = blowjob;
    }

    public boolean isAnal() {
        return anal;
    }

    public void setAnal(boolean anal) {
        this.anal = anal;
    }

}
