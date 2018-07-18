package com.goddardlabs.baketracker.Parcelables;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    public Integer getId() {
        return this.id;
    }

    @SerializedName("name")
    @Expose
    private String name;
    public String getName() {
        return this.name;
    }

    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    public List<Step> getSteps() {
        return this.steps;
    }

    @SerializedName("servings")
    @Expose
    private Integer servings;
    public Integer getServings() {
        return this.servings;
    }

    @SerializedName("image")
    @Expose
    private String image;
    public String getImage() {
        return this.image;
    }

    protected Recipe(Parcel parcel) {
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.servings = parcel.readInt();
        this.image = parcel.readString();

        this.ingredients = new ArrayList<>();
        parcel.readTypedList(ingredients, Ingredient.CREATOR);

        this.steps = new ArrayList<>();
        parcel.readTypedList(steps, Step.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeInt(this.servings);
        parcel.writeString(this.image);
        parcel.writeTypedList(this.ingredients);
        parcel.writeTypedList(this.steps);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}