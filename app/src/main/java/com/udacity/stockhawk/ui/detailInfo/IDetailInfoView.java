package com.udacity.stockhawk.ui.detailInfo;

import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by Oleg Leskin on 26.03.2017.
 */

public interface IDetailInfoView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void showError(int error);

    void fillStockName(String name);

    void fillStockHistoricalInformation(List<HistoricalQuote> historicalQuoteList);
}
