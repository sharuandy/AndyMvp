package com.andy.andymvp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RowsData implements Parcelable {

    private String title;
    private String description;
    private String imageHref;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.imageHref);
    }

    public RowsData() {
    }

    protected RowsData(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.imageHref = in.readString();
    }

    public static final Creator<RowsData> CREATOR = new Creator<RowsData>() {
        @Override
        public RowsData createFromParcel(Parcel source) {
            return new RowsData(source);
        }

        @Override
        public RowsData[] newArray(int size) {
            return new RowsData[size];
        }
    };
}
