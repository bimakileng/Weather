package agromarketday.com.weather.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AgroMarketDay on 9/27/2016.
 */
public class Minute implements Parcelable {
    private long mTime;
    private String mSummary;
    private String mIcon;
    private double mPrecipChance;

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

    public int getIcon() {
        return Forecast.getIconId(mIcon);
    }
    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }
    public void setIcon(String icon) {
        mIcon = icon;
    }

    public double getPrecipChance() {
        return mPrecipChance;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }
    public String getMinute() {
        SimpleDateFormat formatter = new SimpleDateFormat(" mm");
        Date date = new Date(mTime * 1000);
        return formatter.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags){
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeDouble(mPrecipChance);
        dest.writeString(mIcon);

    }
    private Minute(Parcel in){
        mIcon= in.readString();
        mSummary= in.readString();
        mPrecipChance= in.readDouble();
        mTime= in.readLong();
    }
    public Minute(){}
    public static final Parcelable.Creator<Minute> CREATOR = new Parcelable.Creator<Minute>() {
        @Override
        public Minute createFromParcel(Parcel source) {
            return new Minute(source);
        }

        @Override
        public Minute[] newArray(int size) {
            return new Minute[size];
        }
    };


}
