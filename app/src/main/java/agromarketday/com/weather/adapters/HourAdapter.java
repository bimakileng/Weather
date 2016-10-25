package agromarketday.com.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import agromarketday.com.weather.R;
import agromarketday.com.weather.weather.Hour;

/**
 * Created by agromarketday on 9/9/16.
 */
public class HourAdapter extends BaseAdapter {

    private Hour[] mHours;
    private Context mContext;

    public HourAdapter(Context context, Hour[] hours) {
        mContext = context;
        mHours = hours;
    }

    @Override
    public int getCount() {
        return mHours.length;
    }

    @Override
    public Object getItem(int position) {
        return mHours[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; // we aren't going to use this. Tag items for easy reference
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hourly_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.timeLabel = (TextView) convertView.findViewById(R.id.TimeLabel);
            holder.summaryLabel = (TextView) convertView.findViewById(R.id.summaryLabelh);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Hour hour = mHours[position];

        holder.iconImageView.setImageResource(hour.getIconId());
        holder.temperatureLabel.setText(hour.getTemperature() + "");
        holder.summaryLabel.setText(hour.getSummary());

        if (position == 0) {
            holder.timeLabel.setText("Now");
        }
        else {
            holder.timeLabel.setText(hour.getHour());
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView; // public by default
        TextView temperatureLabel;
        TextView timeLabel;
        TextView summaryLabel;
    }
}









