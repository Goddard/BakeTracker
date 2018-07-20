package com.goddardlabs.baketracker.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.goddardlabs.baketracker.Fragments.ListFragment;
import com.goddardlabs.baketracker.Parcelables.Recipe;
import com.goddardlabs.baketracker.Presenter.ListPresenter;
import com.goddardlabs.baketracker.R;
import com.goddardlabs.baketracker.Widgets.WidgetService;

public class MainActivity extends MasterActivity implements ListPresenter.Callbacks {
    WidgetService widgetService;
    public MainActivity() {
        widgetService = new WidgetService();
    }

    @Override
    protected Fragment createFragment() {
        return new ListFragment();
    }

    @Override
    public void recipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getString(R.string.RECIPE_DATA), recipe);

        widgetService.startActionUpdateRecipeWidgets(this, recipe);
        startActivity(intent);
    }
}
