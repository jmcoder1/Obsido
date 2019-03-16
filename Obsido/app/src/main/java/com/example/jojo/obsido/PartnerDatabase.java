package com.example.jojo.obsido;

import android.content.Context;
import android.os.AsyncTask;

import com.example.jojo.obsido.db.Partner;
import com.example.jojo.obsido.db.dao.PartnerDao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Partner.class}, version = 2, exportSchema = false)
@TypeConverters(DateTypeConverter.class)
public abstract class PartnerDatabase extends RoomDatabase {

    private static PartnerDatabase instance;

    public static String DATABASE_NAME = "PartnerDb";

    public abstract PartnerDao partnerDao();

    public static synchronized PartnerDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PartnerDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PartnerDao partnerDao;

        private PopulateDbAsyncTask(PartnerDatabase db) {
            partnerDao = db.partnerDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Could add dummy data here to test
            return null;
        }
    }
}


