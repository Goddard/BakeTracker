package com.goddardlabs.baketracker.Widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.goddardlabs.baketracker.Parcelables.Recipe;
import com.goddardlabs.baketracker.R;

public class WidgetService extends IntentService {
    WidgetProvider widgetProvider;

    public WidgetService() {
        super("WidgetService");

        this.widgetProvider = new WidgetProvider();
    }

    public void startActionUpdateRecipeWidgets(Context context, Recipe recipe) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(context.getString(R.string.RECIPE_WIDGET_UPDATE));
        intent.putExtra(context.getString(R.string.RECIPE_WIDGET_DATA), recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (getString(R.string.RECIPE_WIDGET_UPDATE).equals(action) && intent.getParcelableExtra(getString(R.string.RECIPE_WIDGET_DATA)) != null) {
                handleActionUpdateWidgets((Recipe)intent.getParcelableExtra(getString(R.string.RECIPE_WIDGET_DATA)));
            }
        }
    }

    private void handleActionUpdateWidgets(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        this.widgetProvider.updateRecipeWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }
}