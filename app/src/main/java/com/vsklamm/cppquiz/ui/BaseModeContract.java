package com.vsklamm.cppquiz.ui;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface BaseModeContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
