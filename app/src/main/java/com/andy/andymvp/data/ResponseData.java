package com.andy.andymvp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ResponseData implements Parcelable {
    private String title;
    private List<RowsData> rows;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RowsData> getRows() {
        return rows;
    }

    public void setRows(List<RowsData> rows) {
        this.rows = rows;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeTypedList(this.rows);
    }

    public ResponseData() {
    }

    protected ResponseData(Parcel in) {
        this.title = in.readString();
        this.rows = in.createTypedArrayList(RowsData.CREATOR);
    }

    public static final Creator<ResponseData> CREATOR = new Creator<ResponseData>() {
        @Override
        public ResponseData createFromParcel(Parcel source) {
            return new ResponseData(source);
        }

        @Override
        public ResponseData[] newArray(int size) {
            return new ResponseData[size];
        }
    };
}
