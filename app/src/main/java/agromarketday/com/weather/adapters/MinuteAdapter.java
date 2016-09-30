package agromarketday.com.weather.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import agromarketday.com.weather.R;
import agromarketday.com.weather.weather.Minute;

/**
 * Created by agromarketday on 9/9/16.
 */
public class MinuteAdapter extends RecyclerView.Adapter<MinuteAdapter.MinuteViewHolder> {

    private Minute[] mMinutes;
    private Context mContext;

    public MinuteAdapter(Context context, Minute[] minutes) {
        mContext = context;
        mMinutes = minutes;
    }

    @Override
    public MinuteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.minutely_list_item, parent, false);
        MinuteViewHolder viewHolder = new MinuteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MinuteViewHolder holder, int position) {
        holder.bindMinute(mMinutes[position]);
    }

    @Override
    public int getItemCount() {
        return mMinutes.length;
    }

    public class MinuteViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public TextView mPrecipChanceLabel;
        public ImageView mIconImageView;


        public MinuteViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.TimeLabel);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mPrecipChanceLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);

            itemView.setOnClickListener(this);
        }

        public void bindMinute(Minute minute) {
            mTimeLabel.setText(minute.getMinute());
            mSummaryLabel.setText(minute.getSummary());
            mPrecipChanceLabel.setText((int) minute.getPrecipChance());
            mIconImageView.setImageResource(minute.getIconId());
        }

        @Override
        public void onClick(View v) {
            String time = mTimeLabel.getText().toString();
            String temperature = mPrecipChanceLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String message = String.format("At %s it will be %s and %s",
                    time,
                    temperature,
                    summary);
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
}









