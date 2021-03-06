package com.udacity.stockhawk.ui.detailInfo;

import android.os.AsyncTask;

import com.udacity.stockhawk.R;

import java.util.concurrent.ExecutionException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

/**
 * Created by Oleg Leskin on 26.03.2017.
 */

public class DetailinfoInteractor implements IDetailInfoInteractor {
    @Override
    public void getStockInformation(String symbol, OnGetStockInfoListener listener) {
        Stock stock = null;
        try {
            stock = new GetStockInfoTask().execute(symbol).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            listener.onGetStockInfoError(R.string.error_get_stock_information);
        }

        if (stock != null)
            listener.onGetStockInfoSuccess(stock);
        else listener.onGetStockInfoError(R.string.error_get_stock_information);
    }

    private class GetStockInfoTask extends AsyncTask<String, Void, Stock> {

        @Override
        protected Stock doInBackground(String... strings) {
            try {
                return YahooFinance.get(strings[0], Interval.WEEKLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
