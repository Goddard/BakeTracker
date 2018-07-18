package com.goddardlabs.baketracker.Presenter;

import android.view.View;

import com.goddardlabs.baketracker.Parcelables.Step;

import java.util.ArrayList;

public class DetailPresenter implements DetailPresenterContract.Presenter {

    private final DetailPresenterContract.View mView;

    public interface Callbacks {
        void stepSelected(ArrayList<Step> stepList, int currentStep, String recipeName);
    }

    public DetailPresenter(DetailPresenterContract.View view) {
        this.mView = view;
    }

    @Override
    public void stepClicked(ArrayList<Step> stepList, int currentStep, String recipeName, View view) {
        ((Callbacks) view.getContext()).stepSelected(stepList, currentStep, recipeName);
    }
}

