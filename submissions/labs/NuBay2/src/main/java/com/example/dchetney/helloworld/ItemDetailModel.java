package com.example.dchetney.helloworld;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Observable;

/**
 * Created by dchetney on 1/14/2015.
 */
public class ItemDetailModel extends Observable{
    private int id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private float highestBid;
    private Date startDate;
    private Date endDate;

    public ItemDetailModel() {}

    public ItemDetailModel(int id, String name, String shortDescription, String longDescription, float highestBid, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.highestBid = highestBid;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        setChanged();
        notifyObservers(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers(this);
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        setChanged();
        notifyObservers(this);
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
        setChanged();
        notifyObservers(this);
    }

    public float getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(float highestBid) {
        this.highestBid = highestBid;
        setChanged();
        notifyObservers(this);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        setChanged();
        notifyObservers(this);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        setChanged();
        notifyObservers(this);
    }
}
