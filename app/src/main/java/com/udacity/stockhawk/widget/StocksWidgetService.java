package com.udacity.stockhawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.ui.MainActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Oleg Leskin on 16.04.2017.
 */

public class StocksWidgetService extends IntentService {

    private static final String[] QUOTE_COLUMNS = {
            Contract.Quote._ID,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE
    };

    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;
    private Handler handler = new Handler();;
    private Runnable runnable;

    public StocksWidgetService() {
        super("StocksWidgetService");

        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);


        final Set<String> stockPref = PrefUtils.getStocks(this);

        final Iterator[] stocksIterator = new Iterator[1];

        runnable = new Runnable() {
            @Override
            public void run() {

                if(stocksIterator[0] == null || !stocksIterator[0].hasNext()){
                    stocksIterator[0] = stockPref.iterator();
                }

                Uri stockUri = Contract.Quote.makeUriForStock((String) stocksIterator[0].next());
                Cursor data = getContentResolver().query(stockUri, QUOTE_COLUMNS, null,
                        null, null);

                if (data == null) {
                    return;
                }
                if (!data.moveToFirst()) {
                    data.close();
                    return;
                }

                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(StocksWidgetService.this,
                        StocksWidgetProvider.class));

                String symbol = data.getString(Contract.Quote.POSITION_SYMBOL);
                String price = dollarFormat.format(data.getFloat(Contract.Quote.POSITION_PRICE));
                float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
                String percentage = percentageFormat.format(percentageChange / 100);

                for (int appWidgetId : appWidgetIds) {
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);
                    views.setTextViewText(R.id.symbol, symbol);
                    views.setTextViewText(R.id.price, price);
                    views.setTextViewText(R.id.change, percentage);

                    if (rawAbsoluteChange > 0) {
                        views.setTextColor(R.id.change, getResources().getColor(android.R.color.holo_green_light));
                    } else {
                        views.setTextColor(R.id.change, getResources().getColor(android.R.color.holo_red_light));
                    }

                    // Create an Intent to launch MainActivity
                    Intent launchIntent = new Intent(StocksWidgetService.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(StocksWidgetService.this, 0, launchIntent, 0);
                    views.setOnClickPendingIntent(R.id.widget, pendingIntent);

                    // Tell the AppWidgetManager to perform an update on the current app widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);

                    handler.postDelayed(runnable, 5000);
                }
            }
        };

        handler.postDelayed(runnable, 5000);

    }
}
