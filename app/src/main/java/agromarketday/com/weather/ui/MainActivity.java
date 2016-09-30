package agromarketday.com.weather.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import agromarketday.com.weather.R;
import agromarketday.com.weather.weather.Current;
import agromarketday.com.weather.weather.Day;
import agromarketday.com.weather.weather.Forecast;
import agromarketday.com.weather.weather.Hour;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
   // private static final long MIN_TIME_BW_UPDATES = 1000 ;
    //private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10000;

    private Forecast mForecast;

    @InjectView(R.id.TimeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = 0.3167;
        final double longitude = 32.4167;

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });
        getForecast(latitude, longitude);

        Log.d(TAG, "Main UI code is running!");
    }



    //Method that requests forecast information from the API
    private void getForecast(double latitude,double longitude) {
        String apiKey = getString(R.string.Api_key);
        String forecastUrl = getString(R.string.Url_forecast) + apiKey +
                "/" + latitude + "," + longitude+"?units=si";
        //Checking for network connectivity
        if (isNetworkAvailable()) {
            toggleRefresh();
            //Using okhttp client library to actually request data
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override//Deciding on what to do when the call fails
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override//Deciding on what to do when the call goes through
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    //All the data from the API is manipulated in here
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "JSONException caught: ", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "JSONException caught: ", e);

                    }
                }
            });
        }
        //What is to be done if network is unavailable
        else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    //Special method that refreshes displayed data
    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }
    //Special method that updates displayed information
    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        mTemperatureLabel.setText(current.getTemperature() + " ");
        mTimeLabel.setText( "Updated at "+getTimeAgo(current.getTime()));
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());


        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }
    //Method that creates the forecast object
    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
      //  forecast.setMinutelyForecast(getMinutelyForecast(jsonData));


        return forecast;
    }
    //Method that stores the days forecast in an array
    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        JSONObject daily = forecast.getJSONObject("daily");
        //JSONArray summary = forecast.getJSONArray("summary");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTime(jsonDay.getLong("time"));
            day.setTime(jsonDay.getLong("temperatureMaxTime"));

            days[i] = day;
        }

        return days;
    }
    //Method that stores the hours forecast in an array
    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setTime(jsonHour.getLong("time"));
           // hour.setTimezone(timezone);

            hours[i] = hour;
        }

        return hours;
    }
    //Method that stores the minutes forecast in an array
  /** private Minute[] getMinutelyForecast(String jsonData) throws JSONException{
        JSONObject forecast =new JSONObject(jsonData);
       JSONObject minutely = forecast.getJSONObject("minutely");
        JSONArray data = minutely.getJSONArray("data");

        Minute[] minutes = new Minute[data.length()];
        for (int i =0; i < data.length(); i++){
            JSONObject jsonMinute =data.getJSONObject(i);
            Minute minute = new Minute();

            minute.setSummary(jsonMinute.getString("summary"));
            minute.setTime(jsonMinute.getLong("time"));
            minute.setIcon(jsonMinute.getString("icon"));
            minute.setPrecipChance(jsonMinute.getLong("precipProbability"));
            minutes[i] = minute;

        }
        return minutes;
    }*/
    //Method that stores the current forecast
    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString(getString(R.string.time_zone));
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setLocation("timeZone");

        Log.d(TAG, current.getFormattedTime());

        return current;
    }


    //Method that checks if network is available
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    //Method that alerts the user about an error
    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    //Special on click function from butter knife for the daily button
    @OnClick (R.id.dailyButton)

    public void startDailyActivity() {

            Intent intent = new Intent(this, DailyForecastActivity.class);
            intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
            startActivity(intent);

    }
    //Special on click function from butter knife for the hourly button
    @OnClick (R.id.hourlyButton)
    public void startHourlyActivity() {

            Intent intent = new Intent(this, HourlyForecastActivity.class);
            intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
            startActivity(intent);


    }
    public static String getTimeAgo(long timestamp) {

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(timestamp * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int timeDIM = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (timeDIM == 0) {
            timeAgo = "less than a minute";
        } else if (timeDIM == 1) {
            return "1 minute";
        } else if (timeDIM >= 2 && timeDIM <= 44) {
            timeAgo = timeDIM + " minutes";
        } else if (timeDIM >= 45 && timeDIM <= 89) {
            timeAgo = "about an hour";
        } else if (timeDIM >= 90 && timeDIM <= 1439) {
            timeAgo = "about " + (Math.round(timeDIM / 60)) + " hours";
        } else if (timeDIM >= 1440 && timeDIM <= 2519) {
            timeAgo = "1 day";
        } else if (timeDIM >= 2520 && timeDIM <= 43199) {
            timeAgo = (Math.round(timeDIM / 1440)) + " days";
        } else if (timeDIM >= 43200 && timeDIM <= 86399) {
            timeAgo = "about a month";
        } else if (timeDIM >= 86400 && timeDIM <= 525599) {
            timeAgo = (Math.round(timeDIM / 43200)) + " months";
        } else if (timeDIM >= 525600 && timeDIM <= 655199) {
            timeAgo = "about a year";
        } else if (timeDIM >= 655200 && timeDIM <= 914399) {
            timeAgo = "over a year";
        } else if (timeDIM >= 914400 && timeDIM <= 1051199) {
            timeAgo = "almost 2 years";
        } else {
            timeAgo = "about " + (Math.round(timeDIM / 525600)) + " years";
        }

        return timeAgo + " ago";
    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }
}














