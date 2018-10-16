package com.vsklamm.cppquiz.ui;

import android.support.annotation.NonNull;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface BaseModeContract {

    interface View extends BaseView<Presenter> {

        void onCppStandardChanged(@NonNull final String cppStandard);

        void onErrorHappens();

    }

    interface Presenter extends BasePresenter {

        public void randomQuestion();



    }
}
