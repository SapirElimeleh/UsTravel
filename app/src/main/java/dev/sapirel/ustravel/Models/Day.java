package dev.sapirel.ustravel.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class Day implements Parcelable {

    private String dayName ="";
    private String tripLocation ="";
    private String image =""; //link
    private String date;
    private String tripId;
    private String dayId;

    protected Day(Parcel in) {
        dayName = in.readString();
        tripLocation = in.readString();
        image = in.readString();
        date = in.readString();
        tripId = in.readString();
        dayId =  in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public String getDate() {
        return date;
    }

    public Day setDate(String date) {
        this.date = date;
        return this;
    }

    public Day() {
    }

    public String getDayName() {
        return dayName;
    }

    public Day setDayName(String dayName) {
        this.dayName = dayName;
        return this;
    }

    public String getTripLocation() {
        return tripLocation;
    }

    public Day setTripLocation(String tripLocation) {
        this.tripLocation = tripLocation;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Day setImage(String image) {
        this.image = image;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dayName);
        dest.writeString(tripLocation);
        dest.writeString(image);
        dest.writeString(date);
        dest.writeString(tripId);
        dest.writeString(dayId);
    }


    public Day setTripId(String tripId) {
        this.tripId = tripId;
        return this;
    }

    public String getTripId() {
        return tripId;
    }

    public String getDayId() {
        return dayId;
    }

    public Day setDayId(String dayId) {
        this.dayId = dayId;
        return this;
    }
}
