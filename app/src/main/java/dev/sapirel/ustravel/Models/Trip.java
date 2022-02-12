package dev.sapirel.ustravel.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Trip implements Parcelable {

    private String image = ""; //link
    private String userId = "";
    private String userName = "";
    private String tripLocation ="";
    private ArrayList<Integer> daysIDs;
    private boolean liked = false;
    private boolean myTripVal = false;
    private String startDate = "";
    private String endDate = "";
    private String id = "";
    private int numLikes = 0;


    public Trip() {
        daysIDs = new ArrayList<>();

    }


    protected Trip(Parcel in) {
        image = in.readString();
        userId = in.readString();
        tripLocation = in.readString();
        liked = in.readByte() != 0;
        myTripVal = in.readByte() != 0;
        startDate = in.readString();
        endDate = in.readString();
        id = in.readString();
        userName = in.readString();
        numLikes = in.readInt();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public Trip setMyTripVal(boolean myTripVal) {
        this.myTripVal = myTripVal;
        return this;
    }

    public boolean isMyTripVal() {
        return myTripVal;
    }




    public boolean isLiked() {
        return liked;
    }

    public Trip setLiked(boolean liked) {
        this.liked = liked;
        return this;
    }


    public String getImage() {
        return image;
    }

    public Trip setImage(String image) {
        this.image = image;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Trip setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getTripLocation() {
        return tripLocation;
    }

    public Trip setTripLocation(String tripLocation) {
        this.tripLocation = tripLocation;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(userId);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(tripLocation);
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeByte((byte) (myTripVal ? 1 : 0));
        dest.writeString(id);
        dest.writeString(userName);
        dest.writeInt(numLikes);
    }

    public Trip setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public Trip setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }


    public String getId() {
        return id;
    }

    public Trip setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Trip setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public Trip setNumLikes(int numLikes) {
        this.numLikes = numLikes;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }
}
