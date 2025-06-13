package com.mirelorradie.tesi.homemanager;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {



        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        views.setImageViewResource(R.id.widLogo,R.drawable.iconz);
        views.setImageViewResource(R.id.widMic,R.drawable.mic2);
        views.setImageViewResource(R.id.widPower,R.drawable.power);
        views.setImageViewResource(R.id.widTemp,R.drawable.temperature);

        Intent configIntent = new Intent(context, ComandoVocale.class);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
        views.setOnClickPendingIntent(R.id.widMic, configPendingIntent);

        Intent configIntent2 = new Intent(context, Dispositivi.class);
        PendingIntent configPendingIntent2 = PendingIntent.getActivity(context, 0, configIntent2, 0);
        views.setOnClickPendingIntent(R.id.widPower, configPendingIntent2);

        Intent configIntent3 = new Intent(context, temperatura.class);
        PendingIntent configPendingIntent3 = PendingIntent.getActivity(context, 0, configIntent3, 0);
        views.setOnClickPendingIntent(R.id.widTemp, configPendingIntent3);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);



    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }


    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

