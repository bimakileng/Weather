package agromarketday.com.weather.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by agromarketday on 9/9/16.
 */
public class Day implements Parcelable {
    private long mTime;
    private String mSummary;
    private double mTemperatureMax;
    private String mIcon;
    private long mTemperatureMaxTime;
    private long mSunriseTime;
    private long mSunsetTime;
    private String mTopSummary;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperatureMax() {
        return (int)Math.round(mTemperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax =  temperatureMax ;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTemperatureMaxTime() {
        return mTemperatureMaxTime;
    }

    public void setTemperatureMaxTime(long temperatureMaxTime) {
        mTemperatureMaxTime = temperatureMaxTime;
    }

    public long getSunriseTime() {
        return mSunriseTime;
    }

    public void setSunriseTime(long sunriseTime) {
        mSunriseTime = sunriseTime;
    }

    public long getSunsetTime() {
        return mSunsetTime;
    }

    public void setSunsetTime(long sunsetTime) {
        mSunsetTime = sunsetTime;
    }

    public String getTopSummary() {
        return mTopSummary;
    }

    public void setTopSummary(String topSummary) {
        mTopSummary = topSummary;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT:+03"));
        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeDouble(mTemperatureMax);
        dest.writeString(mIcon);
        dest.writeLong(mTemperatureMaxTime);
        dest.writeLong(mSunriseTime);
        dest.writeLong(mSunsetTime);
        dest.writeString(mTopSummary);
    }

    private Day(Parcel in) {
        mTime = in.readLong();
        mSummary = in.readString();
        mTemperatureMax = in.readDouble();
        mIcon = in.readString();
        mTemperatureMaxTime = in.readLong();
        mSunriseTime = in.readLong();
        mSunsetTime = in.readLong();
        mTopSummary = in.readString();
    }

    public Day() { }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}











