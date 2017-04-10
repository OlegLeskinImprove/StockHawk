package com.udacity.stockhawk.ui.detailInfo;

/**
 * Created by Oleg Leskin on 26.03.2017.
 */

interface IDetailInfoPresenter {

    void getStockInformation(String symbol);

    void onDetachView();
}
