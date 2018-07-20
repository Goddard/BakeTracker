package com.goddardlabs.baketracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.goddardlabs.baketracker.Parcelables.Recipe;
import com.goddardlabs.baketracker.Parcelables.Step;
import com.goddardlabs.baketracker.Fragments.DetailFragment;
import com.goddardlabs.baketracker.Fragments.StepFragment;
import com.goddardlabs.baketracker.Presenter.DetailPresenter;
import com.goddardlabs.baketracker.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DetailPresenter.Callbacks {
    private Recipe currentRecipe;

    public DetailActivity() {
//        this.currentRecipe = getIntent().getExtras().getParcelable(getString(R.string.RECIPE_DATA));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("RESUME_DATA", currentRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);



        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        this.currentRecipe = getIntent().getExtras().getParcelable(getString(R.string.RECIPE_DATA));
        if(savedInstanceState != null) {
            currentRecipe = savedInstanceState.getParcelable("RESUME_DATA");
        }

        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.RECIPE_DATA), currentRecipe);
            fragment = new DetailFragment();
            fragment.setArguments(bundle);

            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();

            if (getResources().getBoolean(R.bool.is_tablet)) {
                Bundle stepBundle = new Bundle();
                stepBundle.putParcelable(getString(R.string.STEP_DATA), currentRecipe.getSteps().get(0));
                StepFragment stepFragment = new StepFragment();
                stepFragment.setArguments(stepBundle);

                Fragment newDetail = stepFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetail).commit();
            }
        }
    }

    @Override
    public void stepSelected(ArrayList<Step> stepList, int currentStep, String recipeName) {
        if (!getResources().getBoolean(R.bool.is_tablet)) {
            Intent intent = new Intent(this, StepsActivity.class);
            intent.putExtra(getString(R.string.STEP_DATA), stepList);
            intent.putExtra(getString(R.string.CURRENT_STEP), currentStep);
            intent.putExtra(getString(R.string.CURRENT_RECIPE), recipeName);

            startActivity(intent);
        } else {
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable(getString(R.string.STEP_DATA), stepList.get(currentStep));
            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(stepBundle);

            Fragment newDetail = stepFragment;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
}
