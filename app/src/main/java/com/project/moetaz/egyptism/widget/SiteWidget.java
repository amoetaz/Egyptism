package com.project.moetaz.egyptism.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.project.moetaz.egyptism.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class SiteWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.site_widget);
        views.setTextViewText(R.id.widget_title
                ,   getTitle(context));
        views.setTextViewText(R.id.widget_desc
                ,  getDesc(context));


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static String getTitle(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_name), MODE_PRIVATE);
        return prefs.getString(context.getString(R.string.widget_title), context.getString(R.string.no_value_msg));
    }

    private static String getDesc(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_name), MODE_PRIVATE);
        return prefs.getString(context.getString(R.string.widget_desc), context.getString(R.string.no_value_msg));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }


    }



