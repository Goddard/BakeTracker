package com.goddardlabs.baketracker.Activities;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.goddardlabs.baketracker.Parcelables.Step;
import com.goddardlabs.baketracker.Fragments.StepFragment;
import com.goddardlabs.baketracker.R;
import com.goddardlabs.baketracker.databinding.ActivityStepViewpagerBinding;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ActivityStepViewpagerBinding binding;

    public StepsActivity() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_step_viewpager);

        final ArrayList<Step> stepList = getIntent().getExtras().getParcelableArrayList(getString(R.string.BUNDLE_STEP_DATA));
        final int currentStep = getIntent().getExtras().getInt(getString(R.string.BUNDLE_CURRENT_STEP));
        String currentRecipeName = getIntent().getExtras().getString(getString(R.string.BUNDLE_CURRENT_RECIPE));

        setSupportActionBar(binding.tbToolbar.toolbar);
        binding.tbToolbar.toolbar.setTitle(currentRecipeName);

        TabLayout tabLayout = findViewById(R.id.tl_activity_step_viewpager);
        for(Step step : stepList) {
            if(step.getId() == 0)
            {
                tabLayout.addTab(tabLayout.newTab().setText("Intro"));
            }
            else {
                tabLayout.addTab(tabLayout.newTab().setText(
                        String.format(getString(R.string.step_number_format), (step.getId()))));
            }
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.tbToolbar.toolbar.setVisibility(View.GONE);
            binding.tlActivityStepViewpager.setVisibility(View.GONE);
        }

        mViewPager = findViewById(R.id.vp_activity_step_viewpager);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Bundle stepBundle = new Bundle();
                stepBundle.putParcelable(getString(R.string.BUNDLE_STEP_DATA), stepList.get(position));
                StepFragment stepFragment = new StepFragment();
                stepFragment.setArguments(stepBundle);

                return stepFragment;
            }

            @Override
            public int getCount() {
                return stepList.size();
            }
        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        mViewPager.setCurrentItem(currentStep);
    }
}
