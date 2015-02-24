package com.example.dchetney.helloworld;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
    private Button toCreate;

    public ItemSearchView(Context context) {
        super(context);
    }

    public ItemSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ItemSearchModel model;

    public void setModel(ItemSearchModel model){
        this.model = model;
        model.addObserver(this);
        bind();
    }

    private ArrayAdapter adapter;

    public void setAdapter(ArrayAdapter adapter){
        this.adapter = adapter;
        list.setAdapter(adapter);
    }

    @Override
    public void update(Observable observable, Object data) {
        bind();
    }

    @Override
    protected void onFinishInflate(){
        searchBar = (SearchView)findViewById(R.id.search_bar);
        list = (ListView)findViewById(R.id.list_item);
        toCreate = (Button)findViewById(R.id.to_create);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return onSearchListener.onSearch(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onSelectListener.onSelect(position);
            }
        });

        toCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onClick(null);
            }
        });
    }

    private void bind(){
        list.setAdapter(new SearchAdapter(getContext(),model.getResults()));
        ((SearchAdapter)list.getAdapter()).notifyDataSetChanged();
    }

    private OnSearchListener onSearchListener;

    public OnSearchListener getOnSearchListener() {
        return onSearchListener;
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    private OnSelectListener onSelectListener;

    public OnSelectListener getOnSelectListener() {
        return onSelectListener;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    private ViewListener viewListener;

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }
}
