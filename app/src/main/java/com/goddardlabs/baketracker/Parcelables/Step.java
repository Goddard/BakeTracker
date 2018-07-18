package com.goddardlabs.baketracker.Parcelables;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    public Integer getId() {
        return this.id;
    }

    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    public String getShortDescription() {
        return this.shortDescription;
    }

    @SerializedName("description")
    @Expose
    private String description;
    public String getDescription() {
        return this.description;
    }

    @SerializedName("videoURL")
    @Expose
    private String videoURL;
    public String getVideoURL() {
        return this.videoURL;
    }

    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;
    public String getThumbnailURL() {
        return this.thumbnailURL;
    }

    private Step(Parcel parcel) {
        this.id = parcel.readInt();
        this.shortDescription = parcel.readString();
        this.description = parcel.readString();
        this.videoURL = parcel.readString();
        this.thumbnailURL = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.id);
        parcel.writeString(this.shortDescription);
        parcel.writeString(this.description);
        parcel.writeString(this.videoURL);
        parcel.writeString(this.thumbnailURL);
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}

