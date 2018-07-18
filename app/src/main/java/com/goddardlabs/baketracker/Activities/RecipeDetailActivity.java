package com.goddardlabs.baketracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.goddardlabs.baketracker.Parcelables.Recipe;
import com.goddardlabs.baketracker.Parcelables.Step;
import com.goddardlabs.baketracker.Fragments.DetailFragment;
import com.goddardlabs.baketracker.Fragments.StepFragment;
import com.goddardlabs.baketracker.Presenter.DetailPresenter;
import com.goddardlabs.baketracker.R;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements DetailPresenter.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        Recipe recipe = getIntent().getExtras().getParcelable(getString(R.string.BUNDLE_RECIPE_DATA));

        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.BUNDLE_RECIPE_DATA), recipe);
            fragment = new DetailFragment();
            fragment.setArguments(bundle);

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();

            if (getResources().getBoolean(R.bool.isTablet)) {
                Bundle stepBundle = new Bundle();
                stepBundle.putParcelable(getString(R.string.BUNDLE_STEP_DATA), recipe.getSteps().get(0));
                StepFragment stepFragment = new StepFragment();
                stepFragment.setArguments(stepBundle);

                Fragment newDetail = stepFragment;
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
            }
        }
    }

    @Override
    public void stepSelected(ArrayList<Step> stepList, int currentStep, String recipeName) {
        if (!getResources().getBoolean(R.bool.isTablet)) {
            Intent intent = new Intent(this, StepsActivity.class);
            intent.putExtra(getString(R.string.BUNDLE_STEP_DATA), stepList);
            intent.putExtra(getString(R.string.BUNDLE_CURRENT_STEP), currentStep);
            intent.putExtra(getString(R.string.BUNDLE_CURRENT_RECIPE), recipeName);

            startActivity(intent);
        } else {
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable(getString(R.string.BUNDLE_STEP_DATA), stepList.get(currentStep));
            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(stepBundle);

            Fragment newDetail = stepFragment;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
}
