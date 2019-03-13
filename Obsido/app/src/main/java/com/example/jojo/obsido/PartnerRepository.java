package com.example.jojo.obsido;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class PartnerRepository {

    private PartnerDao partnerDao;
    private LiveData<List<Partner>> allPartners;

    public PartnerRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        partnerDao = db.partnerDao();
        allPartners = partnerDao.getAllPartners();
    }

    public void insert(Partner partner) {
        new InsertPartnerAsyncTask(partnerDao).execute(partner);
    }

    public void update(Partner partner) {
        new UpdatePartnerAsyncTask(partnerDao).execute(partner);
    }

    public void delete(Partner partner) {
        new DeletePartnerAsyncTask(partnerDao).execute(partner);
    }

    public void deleteAllPartners() {
        new DeleteAllPartnerAsyncTask(partnerDao).execute();
    }

    public LiveData<List<Partner>> getAllPartners() {
        return allPartners;
    }

    private static class InsertPartnerAsyncTask extends AsyncTask<Partner, Void, Void> {
        private PartnerDao partnerDao;

        private InsertPartnerAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        protected Void doInBackground(Partner... partners) {
            partnerDao.insert(partners[0]);
            return null;
        }
    }

    private static class UpdatePartnerAsyncTask extends AsyncTask<Partner, Void, Void> {
        private PartnerDao partnerDao;

        private UpdatePartnerAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        protected Void doInBackground(Partner... partners) {
            partnerDao.update(partners[0]);
            return null;
        }
    }

    private static class DeletePartnerAsyncTask extends AsyncTask<Partner, Void, Void> {
        private PartnerDao partnerDao;

        private DeletePartnerAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        protected Void doInBackground(Partner... partners) {
            partnerDao.delete(partners[0]);
            return null;
        }
    }

    private static class DeleteAllPartnerAsyncTask extends AsyncTask<Partner, Void, Void> {
        private PartnerDao partnerDao;

        private DeleteAllPartnerAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        protected Void doInBackground(Partner... partners) {
            partnerDao.deleteAllPartners();
            return null;
        }
    }
}
