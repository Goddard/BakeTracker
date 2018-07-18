package com.goddardlabs.baketracker.Presenter;

import com.goddardlabs.baketracker.Parcelables.Step;

import java.util.ArrayList;

public interface DetailPresenterContract {
    interface View {}

    interface Presenter {
        void stepClicked(ArrayList<Step> stepList, int currentStep, String recipeName, android.view.View view);
    }
}