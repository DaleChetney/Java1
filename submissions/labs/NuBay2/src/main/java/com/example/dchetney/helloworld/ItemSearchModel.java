package com.example.dchetney.helloworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by dchetney on 1/20/2015.
 */
public class ItemSearchModel extends Observable{
    private ArrayList<ItemDetailModel> results;

    public ItemSearchModel() {results = new ArrayList<ItemDetailModel>();}
    public ItemSearchModel(ArrayList<ItemDetailModel> results) {this.results = results;}

    public ArrayList<ItemDetailModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<ItemDetailModel> results) {
        this.results = results;
        setChanged();
        notifyObservers(this);
    }


}
