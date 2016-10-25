package agromarketday.com.weather.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.Arrays;

import agromarketday.com.weather.R;
import agromarketday.com.weather.adapters.HourAdapter;
import agromarketday.com.weather.weather.Hour;

public class HourlyForecastActivity extends ListActivity {

    private Hour[] mHours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter = new HourAdapter(this, mHours);
        setListAdapter(adapter);
    }
}

