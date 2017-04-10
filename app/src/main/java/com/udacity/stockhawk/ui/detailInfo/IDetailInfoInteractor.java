package com.udacity.stockhawk.ui.detailInfo;

import android.support.annotation.StringRes;

import yahoofinance.Stock;

/**
 * Created by Oleg Leskin on 26.03.2017.
 */

public interface IDetailInfoInteractor {

    interface OnGetStockInfoListener {
        void onGetStockInfoSuccess(Stock stock);

        void onGetStockInfoError(String error);

        void onGetStockInfoError(@StringRes int error);
    }

    void getStockInformation(String symbol, OnGetStockInfoListener listener);
}
