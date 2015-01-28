package com.example.dchetney.helloworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dchetney on 1/27/2015.
 */
public class SearchAdapter extends ArrayAdapter {
    public SearchAdapter(Context context, List<ItemDetailModel> list){
        super(context, R.layout.search_sublayout ,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater  = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.search_sublayout,parent,false);
        ItemDetailModel model = ((ItemDetailModel)getItem(position));
        TextView name = (TextView)view.findViewById(R.id.title);
        name.setText(model.getName());
        TextView price = (TextView)view.findViewById(R.id.price);
        price.setText("$"+model.getHighestBid());
        return view;
    }
}
