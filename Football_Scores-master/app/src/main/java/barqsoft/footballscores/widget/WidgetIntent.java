package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Kevin on 23.10.2015.
 */
public class WidgetIntent extends IntentService {

    public WidgetIntent() {
        super("WidgetIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("widget", "handle intent");

        // Retrieve all of the widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));

        // get todays games from the content provider
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);
            views.setTextViewText(R.id.widget_title, "Today");
            Intent mIntent = new Intent(this, WidgetService.class);
            mIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            mIntent.setData((Uri.parse(mIntent.toUri(Intent.URI_INTENT_SCHEME))));
            views.setRemoteAdapter(R.id.listViewWidget, mIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }



    }


}
