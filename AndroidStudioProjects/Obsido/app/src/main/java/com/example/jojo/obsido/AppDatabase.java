package com.example.jojo.obsido;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Partner.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract PartnerDao partnerDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "partner_database")
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

        private PopulateDbAsyncTask(AppDatabase db) {
            partnerDao = db.partnerDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            partnerDao.insert(new Partner("Example 1", "This is a test", 18, 0));
            partnerDao.insert(new Partner("Example 2", "This is a test", 21, 1));
            partnerDao.insert(new Partner("Example 3", "This is a test", 100, 0));
            partnerDao.insert(new Partner("Example 4", "This is a test", 18, 0));
            partnerDao.insert(new Partner("Example 5", "This is a test", 21, 1));
            partnerDao.insert(new Partner("Example 6", "This is a test", 100, 0));
            partnerDao.insert(new Partner("Example 7", "This is a test", 18, 0));
            partnerDao.insert(new Partner("Example 8", "This is a test", 21, 1));
            partnerDao.insert(new Partner("Example 9", "This is a test", 100, 0));
            return null;
        }
    }
}


