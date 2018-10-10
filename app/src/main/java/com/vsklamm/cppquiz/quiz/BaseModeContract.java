package com.vsklamm.cppquiz.quiz;

public interface BaseModeContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
