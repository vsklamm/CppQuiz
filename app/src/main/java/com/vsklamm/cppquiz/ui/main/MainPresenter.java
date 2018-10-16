package com.vsklamm.cppquiz.ui.main;

import com.vsklamm.cppquiz.data.MainRepository;
import com.vsklamm.cppquiz.ui.BaseModeContract;

import io.reactivex.annotations.NonNull;

public class MainPresenter implements QuizModeContract.Presenter, TrainingModeContract.Presenter {

    public MainPresenter(@NonNull MainRepository mainRepository,
                         @NonNull BaseModeContract.View mainView) {

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
