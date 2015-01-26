package com.example.dchetney.helloworld;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by dchetney on 1/22/2015.
 */
public class ItemSearchView extends LinearLayout implements Observer{
    private SearchView searchBar;
    private ListView list;

    private ItemSearchModel model;

    public ItemSearchView(Context context) {
        super(context);
    }

    public ItemSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setModel(ItemSearchModel model)
    {
        this.model = model;
        model.addObserver(this);
        bind(model);
    }

    @Override
    public void update(Observable observable, Object data) {
        bind(model);
    }

    @Override
    protected void onFinishInflate(){
        searchBar = (SearchView)findViewById(R.id.search_bar);
        list = (ListView)findViewById(R.id.list_item);
    }

    private void bind(ItemSearchModel model){

    }

    private ViewListener viewListener;

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }


    private OnChangeListener<ItemSearchModel> listener = new OnChangeListener<ItemSearchModel>(){

        @Override
        public void onChange(ItemSearchModel model) {
            bind(model);
        }
    };
}
