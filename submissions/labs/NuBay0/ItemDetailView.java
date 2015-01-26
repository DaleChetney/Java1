package com.example.dchetney.helloworld;

import android.content.Context;
import android.media.Image;
import android.net.sip.SipAudioCall;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by dchetney on 1/14/2015.
 */
public class ItemDetailView extends LinearLayout implements Observer{

    private Image itemImage;
    private TextView nameLabel;
    private TextView descriptionLabel;
    private TextView priceLabel;
    private TextView priceValueLabel;
    private Button bidButton;

    private ItemDetailModel model;

    public ItemDetailView(Context context) {
        super(context);
    }

    public ItemDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setModel(ItemDetailModel model)
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
        nameLabel = (TextView)findViewById(R.id.title);
        descriptionLabel = (TextView)findViewById(R.id.description);
        priceLabel = (TextView)findViewById(R.id.current_price);
        priceValueLabel = (TextView)findViewById(R.id.price);
        bidButton = (Button)findViewById(R.id.bid);

        bidButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onClick(model);
            }
        });
    }

    private void bind(ItemDetailModel model){
        nameLabel.setText(model.getName());
        descriptionLabel.setText(model.getLongDescription());
        priceValueLabel.setText(String.valueOf(model.getHighestBid()));
    }

    private ViewListener viewListener;

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }


    private OnChangeListener<ItemDetailModel> listener = new OnChangeListener<ItemDetailModel>(){

        @Override
        public void onChange(ItemDetailModel model) {
            bind(model);
        }
    };
}
