package com.goddardlabs.baketracker.Presenter;

import android.support.annotation.StringRes;

import com.goddardlabs.baketracker.Parcelables.Recipe;

import java.util.ArrayList;

public interface ListPresenterContract {
    interface View {

        void updateAdapter(ArrayList<Recipe> recipeList);

        void displaySnackbarMessage(@StringRes int stringResId);

        boolean isActive();

    }

    interface Presenter {

        void fetchRecipes();

        void recipeClicked(Recipe recipe, android.view.View view);

        void viewDestroy();

    }
}
