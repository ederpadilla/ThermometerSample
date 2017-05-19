package com.eder.padilla.alanproject.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ederpadilla on 18/05/17.
 */

public class GraphDataPoint extends RealmObject {

    @PrimaryKey
    int id;

    String mDataPoint;

    String mDate;

    String mHour;

    public GraphDataPoint() {
    }

    public GraphDataPoint(int id, String mDataPoint, String mDate, String mHour) {
        this.id = id;
        this.mDataPoint = mDataPoint;
        this.mDate = mDate;
        this.mHour = mHour;
    }

    public int getId() {
        return id;
    }

    public String getmDataPoint() {
        return mDataPoint;
    }

    public String getmDate() {
        return mDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setmDataPoint(String mDataPoint) {
        this.mDataPoint = mDataPoint;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmHour() {
        return mHour;
    }

    public void setmHour(String mHour) {
        this.mHour = mHour;
    }

    @Override
    public String toString() {
        return "GraphDataPoint{" +
                "id=" + id +
                ", mDataPoint='" + mDataPoint + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mHour='" + mHour + '\'' +
                '}';
    }
}
