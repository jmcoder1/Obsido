package com.example.jojo.obsido.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "partners")
public class Partner {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "partner_name")
    private String name;

    @ColumnInfo(name = "partner_description")
    private String description;

    @ColumnInfo(name = "partner_age")
    private int age;

    @ColumnInfo(name = "partner_gender")
    private int gender;

    public Partner(String name, String description, int age, int gender) {
        this.name = name;
        this.description = description;
        this.age = age;
        this.gender = gender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }
}
