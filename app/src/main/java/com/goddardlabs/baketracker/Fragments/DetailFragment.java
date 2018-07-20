package com.goddardlabs.baketracker.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.goddardlabs.baketracker.Adapters.StepAdapter;
import com.goddardlabs.baketracker.Parcelables.Ingredient;
import com.goddardlabs.baketracker.Parcelables.Recipe;
import com.goddardlabs.baketracker.Parcelables.Step;
import com.goddardlabs.baketracker.Presenter.DetailPresenter;
import com.goddardlabs.baketracker.Presenter.DetailPresenterContract;
import com.goddardlabs.baketracker.R;
import com.goddardlabs.baketracker.Adapters.OnItemClickListener;
import com.goddardlabs.baketracker.databinding.FragmentRecipeDetailBinding;

import java.util.ArrayList;

public class DetailFragment extends Fragment implements DetailPresenterContract.View {
    private FragmentRecipeDetailBinding binding;
    private int mStepAdapterSavedPosition = 0;
    private ArrayList<Step> mStepList = new ArrayList<>();
    private ArrayList<TextView> mIngredientList = new ArrayList<>();
    private Recipe mRecipe;
    private DetailPresenter mDetailPresenter;
    private RecyclerView mStepRecyclerView;
    private StepAdapter mStepAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if ((arguments != null) && (arguments.containsKey(getString(R.string.RECIPE_DATA)))) {
            mRecipe = arguments.getParcelable(getString(R.string.RECIPE_DATA));
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.KEY_ADAPTER_POSITION))) {
            mStepAdapterSavedPosition = savedInstanceState.getInt(getString(R.string.KEY_ADAPTER_POSITION));
        }

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(getString(R.string.KEY_RECIPE))) {
                mRecipe = savedInstanceState.getParcelable(getString(R.string.KEY_RECIPE));
            }

            if(savedInstanceState.containsKey(getString(R.string.KEY_STEPS))) {
                mStepList = savedInstanceState.getParcelableArrayList(getString(R.string.KEY_STEPS));
            }

            if(savedInstanceState.containsKey(getString(R.string.KEY_INGREDIENTS_COUNT))) {
                int ingredientCount = savedInstanceState.getInt(getString(R.string.KEY_INGREDIENTS_COUNT));
                for(int i=0; i < ingredientCount; i++) {
                    CheckBox checkBox = new CheckBox(this.getContext());
                    checkBox.setTextColor(getResources().getColor(R.color.white));
                    checkBox.setPadding(10,12,10,12);
                    String ingredientLine = "- " + String.valueOf(mRecipe.getIngredients().get(i).getQuantity()) + String.valueOf(mRecipe.getIngredients().get(i).getMeasure()) + " " + mRecipe.getIngredients().get(i).getIngredient();

                    checkBox.setText(ingredientLine);
                    binding.llIngredientChecklist.addView(checkBox);
                }
            }
        }
        mDetailPresenter = new DetailPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
        final View view = binding.getRoot();

        binding.toolbarContainer.toolbar.setTitle(mRecipe.getName());
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbarContainer.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(mIngredientList.size() > 0) {
            for(TextView tvIngredientView : mIngredientList) {
                binding.llIngredientChecklist.addView(tvIngredientView);
            }
        } else {
            if (mRecipe.getIngredients() != null && mRecipe.getIngredients().size() > 0) {
                for (Ingredient ingredient : mRecipe.getIngredients()) {
                    CheckBox checkBox = new CheckBox(this.getContext());
                    checkBox.setTextColor(getResources().getColor(R.color.white));
                    checkBox.setPadding(10,12,10,12);
                    String ingredientLine = String.valueOf(ingredient.getQuantity()) + String.valueOf(ingredient.getMeasure()) + " " + ingredient.getIngredient();

                    checkBox.setText(ingredientLine);
                    binding.llIngredientChecklist.addView(checkBox);
                }
            }
        }

        if (mRecipe.getSteps() != null && mRecipe.getSteps().size() > 0 && mStepList.size()==0) {
            mStepList.addAll(mRecipe.getSteps());
        }

        mStepRecyclerView = view.findViewById(R.id.recipe_detail_steps_recycler_view);
        mStepRecyclerView.setHasFixedSize(true);
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mStepAdapter = new StepAdapter(mStepList, new OnItemClickListener<Step>() {
            @Override
            public void onClick(Step step, View view) {
                mDetailPresenter.stepClicked(mStepList, step.getId(), mRecipe.getName(), view);
            }
        });

        mStepAdapter.setStepCurrentPosition(mStepAdapterSavedPosition);
        mStepRecyclerView.setAdapter(mStepAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.KEY_ADAPTER_POSITION), mStepAdapter.getStepCurrentPosition());
        outState.putParcelable(getString(R.string.KEY_RECIPE), mRecipe);
        outState.putParcelableArrayList(getString(R.string.KEY_STEPS), mStepList);
        outState.putInt(getString(R.string.KEY_INGREDIENTS_COUNT), mIngredientList.size());
    }
}

