package com.example.dchetney.helloworld;

import java.util.Observable;

/**
 * Created by dchetney on 1/20/2015.
 */
public class ItemSearchModel extends Observable{
    private String query;

    public ItemSearchModel() {}

    public ItemSearchModel(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


}
