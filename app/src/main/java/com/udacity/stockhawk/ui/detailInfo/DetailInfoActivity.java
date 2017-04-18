package com.udacity.stockhawk.ui.detailInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by Oleg Leskin on 25.03.2017.
 */

public class DetailInfoActivity extends AppCompatActivity implements IDetailInfoView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.coordinator_layout)
    View parentView;

    @BindView(R.id.text_not_found)
    TextView notFoundText;

    @BindView(R.id.list_history)
    RecyclerView historyList;

    private String symbol;
    private IDetailInfoPresenter presenter;

    public static void launch(Context context, String symbol) {
        Intent intent = new Intent(context, DetailInfoActivity.class);
        intent.putExtra("symbol", symbol);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_information);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getExtra();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new DetailinfoPresenter(this);
        presenter.getStockInformation(symbol);
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            symbol = getIntent().getStringExtra("symbol");
        }
    }

    @Override
    public void fillStockName(String name) {
        getSupportActionBar().setTitle(name);
    }

    @Override
    public void fillStockHistoricalInformation(List<HistoricalQuote> historicalQuoteList) {
        historyList.setLayoutManager(new LinearLayoutManager(this));
        if(historicalQuoteList.isEmpty())
            notFoundText.setVisibility(View.VISIBLE);
        else{
            notFoundText.setVisibility(View.GONE);
            historyList.setAdapter(new StockHistoryAdapter(historicalQuoteList));
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(parentView, error, BaseTransientBottomBar.LENGTH_LONG);
    }

    @Override
    public void showError(int error) {
        Snackbar.make(parentView, error, BaseTransientBottomBar.LENGTH_LONG);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetachView();
        super.onDestroy();
    }
}
