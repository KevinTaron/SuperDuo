package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context context = null;
    private int appWidgetId;

    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL
    };

    private List<List> widgetList = new ArrayList<>();

    public WidgetRemoteViewsFactory(Context context, Intent intent)
    {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d("AppWidgetId", String.valueOf(appWidgetId));
    }

    private void updateWidgetListView()
    {
        Uri scoresUri = DatabaseContract.scores_table.buildScoreWithDate();
        Cursor mCursor = null;
        if(context == null) {
            return;
        }

        try {
            mCursor = context.getContentResolver().query(scoresUri, SCORES_COLUMNS, null, getDate(), DatabaseContract.scores_table.DATE_COL);
        } catch(Exception e) {
            Log.i("widget", e.toString());
        }


        if(mCursor == null) {
            return;
        }

        List<List> scoresItem = new ArrayList<>();
        while(mCursor.moveToNext()) {
            List<String> matchItem = new ArrayList<>();

            matchItem.add(0, mCursor.getString(2));
            matchItem.add(1, mCursor.getString(3));
            matchItem.add(2, mCursor.getString(4));
            matchItem.add(3, mCursor.getString(5));
            matchItem.add(4, mCursor.getString(1));

            scoresItem.add(matchItem);
        }

        this.widgetList = scoresItem;
    }

    @Override
    public int getCount()
    {
        return widgetList.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_row_layout);

        List<String> matchItem = widgetList.get(position);

        remoteView.setTextViewText(R.id.home_name, matchItem.get(0));
        remoteView.setTextViewText(R.id.away_name, matchItem.get(1));
        remoteView.setTextViewText(R.id.score_textview, Utilies.getScores(Integer.getInteger(matchItem.get(2), -1), Integer.getInteger(matchItem.get(3), -1)));
        remoteView.setTextViewText(R.id.data_textview, matchItem.get(4));




//        Log.d("Loading", widgetList.get(position));
//        remoteView.setTextViewText(R.id.textView, widgetList.get(position));

        return remoteView;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public void onCreate()
    {
        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged()
    {
        updateWidgetListView();
    }

    @Override
    public void onDestroy()
    {
        widgetList.clear();
    }

    private String[] getDate() {
        Date fragmentdate = new Date(System.currentTimeMillis()+((0)*86400000));
        SimpleDateFormat mformate = new SimpleDateFormat("yyyy-MM-dd");
        String[] date = new String[1];
        date[0] = mformate.format(fragmentdate);

        return date;
    }
}