package com.goddardlabs.baketracker.Parcelables;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {
    @SerializedName("quantity")
    @Expose
    private Float quantity;
    public Float getQuantity() {
        return this.quantity;
    }

    @SerializedName("measure")
    @Expose
    private String measure;
    public String getMeasure() {
        return this.measure;
    }

    @SerializedName("ingredient")
    @Expose
    private String ingredient;
    public String getIngredient() {
        return this.ingredient;
    }

    private Ingredient(Parcel parcel) {
        this.quantity = parcel.readFloat();
        this.measure = parcel.readString();
        this.ingredient = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeFloat(this.quantity);
        parcel.writeString(this.measure);
        parcel.writeString(this.ingredient);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}