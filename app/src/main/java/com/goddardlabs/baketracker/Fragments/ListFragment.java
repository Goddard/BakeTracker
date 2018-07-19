package com.goddardlabs.baketracker.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goddardlabs.baketracker.Adapters.RecipeAdapter;
import com.goddardlabs.baketracker.Parcelables.Recipe;
import com.goddardlabs.baketracker.Net.NetworkService;
import com.goddardlabs.baketracker.Presenter.ListPresenter;
import com.goddardlabs.baketracker.Presenter.ListPresenterContract;
import com.goddardlabs.baketracker.R;
import com.goddardlabs.baketracker.Adapters.OnItemClickListener;
import com.goddardlabs.baketracker.databinding.FragmentRecipeListBinding;

import java.util.ArrayList;

public class ListFragment extends Fragment implements ListPresenterContract.View {
    private FragmentRecipeListBinding binding;
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private ListPresenter mListPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(getString(R.string.KEY_RECIPE_LIST))) {
                mRecipeList = savedInstanceState.getParcelableArrayList(getString(R.string.KEY_RECIPE_LIST));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);
        final View view = binding.getRoot();

        mRecipeRecyclerView = view.findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setHasFixedSize(true);
        mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.recipe_columns)));

        mListPresenter = new ListPresenter(this, new NetworkService());

        mAdapter = new RecipeAdapter(mRecipeList, new OnItemClickListener<Recipe>() {
            @Override
            public void onClick(Recipe recipe, View view) {
                mListPresenter.recipeClicked(recipe, view);
            }
        });
        mRecipeRecyclerView.setAdapter(mAdapter);

        //TODO:swipe to force refresh?
        if(mRecipeList==null || mRecipeList.size()==0) {
            mListPresenter.fetchRecipes();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListPresenter.viewDestroy();
    }

    @Override
    public void updateAdapter(ArrayList<Recipe> recipeList) {
        mRecipeList.clear();
        mRecipeList.addAll(recipeList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void displaySnackbarMessage(int stringResId) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.view_page_list_container), getString(stringResId), Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        snackbar.show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.KEY_RECIPE_LIST), mRecipeList);
        super.onSaveInstanceState(outState);
    }
}
