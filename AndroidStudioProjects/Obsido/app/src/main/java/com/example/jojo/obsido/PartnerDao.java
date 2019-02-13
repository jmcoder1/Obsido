package com.example.jojo.obsido;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PartnerDao {

    @Insert
    void insert(Partner partner);

    @Update
    void update(Partner partner);

    @Delete
    void delete(Partner partner);

    @Query("DELETE FROM partners")
    void deleteAllPartners();

    @Query("SELECT * FROM partners")
    LiveData<List<Partner>> getAllPartners();
}
