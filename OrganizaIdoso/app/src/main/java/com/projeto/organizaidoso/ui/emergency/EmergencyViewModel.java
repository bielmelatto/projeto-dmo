package com.projeto.organizaidoso.ui.emergency;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmergencyViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public EmergencyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}