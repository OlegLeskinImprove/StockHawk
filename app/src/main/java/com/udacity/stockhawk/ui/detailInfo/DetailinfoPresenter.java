package com.udacity.stockhawk.ui.detailInfo;

import android.support.annotation.StringRes;

import com.udacity.stockhawk.R;

import java.io.IOException;

import timber.log.Timber;
import yahoofinance.Stock;

/**
 * Created by Oleg Leskin on 26.03.2017.
 */

public class DetailinfoPresenter implements IDetailInfoPresenter,
        IDetailInfoInteractor.OnGetStockInfoListener {

    private IDetailInfoView view;
    private IDetailInfoInteractor interactor;

    public DetailinfoPresenter(IDetailInfoView view) {
        this.view = view;
        interactor = new DetailinfoInteractor();
    }

    @Override
    public void getStockInformation(String symbol) {
        if (view == null) return;

        view.showProgress();
        interactor.getStockInformation(symbol, this);
    }

    @Override
    public void onGetStockInfoSuccess(Stock stock) {
        if (view == null) return;
        view.hideProgress();
        view.fillStockName(stock.getName());
        try {
            view.fillStockHistoricalInformation(stock.getHistory());
        } catch (IOException e) {
            Timber.e(e.getMessage());
            view.showError(R.string.error_get_stock_information);
        }
    }

    @Override
    public void onGetStockInfoError(String error) {
        if (view == null) return;
        view.hideProgress();
        view.showError(error);
    }

    @Override
    public void onGetStockInfoError(@StringRes int error) {
        if (view == null) return;
        view.hideProgress();
        view.showError(error);
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
