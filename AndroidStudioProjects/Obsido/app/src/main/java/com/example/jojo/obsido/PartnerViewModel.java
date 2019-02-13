package com.example.jojo.obsido;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PartnerViewModel extends AndroidViewModel {
    private PartnerRepository repository;
    private LiveData<List<Partner>> allPartners;

    public PartnerViewModel(@NonNull Application application) {
        super(application);
        repository = new PartnerRepository(application);
        allPartners = repository.getAllPartners();
    }

    public void insert(Partner partner) {
        repository.insert(partner);
    }

    public void update(Partner partner) {
        repository.update(partner);
    }

    public void delete(Partner partner) {
        repository.delete(partner);
    }

    public void deleteAllNotes() {
        repository.deleteAllPartners();
    }

    public LiveData<List<Partner>> getAllPartners() {
        return allPartners;
    }
}
