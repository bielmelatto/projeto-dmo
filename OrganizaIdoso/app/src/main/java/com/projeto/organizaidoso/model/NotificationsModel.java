package com.projeto.organizaidoso.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NotificationsModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Sem notificações");
    }

    public LiveData<String> getText() {
        return mText;
    }
}