package com.udacity.stockhawk.ui.detailInfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by Oleg Leskin on 26.03.2017.
 */

class StockHistoryAdapter extends RecyclerView.Adapter<StockHistoryAdapter.HistoryViewHolder> {
    private List<HistoricalQuote> quoteList;
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());

    public StockHistoryAdapter(List<HistoricalQuote> quoteList) {
        this.quoteList = quoteList;
    }

    @Override
    public StockHistoryAdapter.HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        this.context = parent.getContext();
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.container.setBackgroundResource(position % 2 == 0 ? R.color.grayLight : android.R.color.white);

        if (position == 0) {
            holder.date.setText(R.string.item_history_date);
            holder.openCost.setText(R.string.item_history_open_amount);
            holder.highCost.setText(R.string.item_history_high_amount);
            holder.lowCost.setText(R.string.item_history_low_amount);
            holder.closeCost.setText(R.string.item_history_close_amount);

            holder.container.setBackgroundResource(R.color.colorAccent);
        } else {
            HistoricalQuote quote = getItem(position);
            holder.date.setText(sdf.format(quote.getDate().getTime()));
            holder.openCost.setText(Html.fromHtml(String.format(context.getString(R.string.amount_with_dollar),
                    String.valueOf(quote.getOpen().setScale(2, RoundingMode.HALF_UP)))));
            holder.highCost.setText(String.format(context.getString(R.string.amount_with_dollar),
                    String.valueOf(quote.getHigh().setScale(2, RoundingMode.HALF_UP))));
            holder.lowCost.setText(String.format(context.getString(R.string.amount_with_dollar),
                    String.valueOf(quote.getLow().setScale(2, RoundingMode.HALF_UP))));
            holder.closeCost.setText(String.format(context.getString(R.string.amount_with_dollar),
                    String.valueOf(quote.getClose().setScale(2, RoundingMode.HALF_UP))));
        }
    }

    private HistoricalQuote getItem(int position) {
        if (position > 0)
            return quoteList.get(position - 1);
        return quoteList.get(position);
    }

    @Override
    public int getItemCount() {
        return quoteList.size() + 1;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_date)
        TextView date;
        @BindView(R.id.text_open)
        TextView openCost;
        @BindView(R.id.text_high)
        TextView highCost;
        @BindView(R.id.text_low)
        TextView lowCost;
        @BindView(R.id.text_close)
        TextView closeCost;

        @BindView(R.id.container)
        ViewGroup container;

        HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
