package com.example.myitinerary.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> email;

    public ProfileViewModel() {
        email = new MutableLiveData<>();
        email.setValue("Auto-fill email");
    }

    public LiveData<String> getText() { return email; }
}