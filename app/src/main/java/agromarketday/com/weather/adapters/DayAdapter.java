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
import agromarketday.com.weather.weather.Day;

/**
 * Created by agromarketday on 9/9/16.
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private Day[] mDays;
    private Context mContext;

    public DayAdapter(Context context, Day[] days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_list_item, parent, false);
        DayViewHolder viewHolder = new DayViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        holder.bindDay(mDays[position]);
    }

    @Override
    public int getItemCount() {
        return mDays.length;
    }

    public class DayViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public TextView mTemperatureLabel;
        public ImageView mIconImageView;

        public DayViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.TimeLabel);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);

            itemView.setOnClickListener(this);
        }

        public void bindDay(Day day) {
            mTimeLabel.setText( day.getDayOfTheWeek());
            mSummaryLabel.setText(day.getSummary());
            mTemperatureLabel.setText(day.getTemperatureMax() + "");
            mIconImageView.setImageResource(day.getIconId());
        }

        @Override
        public void onClick(View v) {
            String time = mTimeLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String message = String.format("At %s it will be %s and %s",
                    time,
                    temperature,
                    summary);
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
}









