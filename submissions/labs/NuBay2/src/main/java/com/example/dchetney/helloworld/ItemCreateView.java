package com.example.dchetney.helloworld;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by dchetney on 2/3/2015.
 */
public class ItemCreateView extends LinearLayout implements Observer {

    EditText nameForm;
    EditText descriptionForm;
    EditText startingBidForm;
    EditText startDateForm;
    EditText endDateForm;
    Button createButton;
    TextView errorMessage;

    ItemDetailModel model;

    private ViewListener viewListener;

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public void setModel(ItemDetailModel model) {
        this.model = model;
        model.addObserver(this);
        bind();
    }

    public ItemCreateView(Context context) {
        super(context);
    }

    public ItemCreateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemCreateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        nameForm = (EditText)findViewById(R.id.name_form);
        descriptionForm = (EditText)findViewById(R.id.description_form);
        startingBidForm = (EditText)findViewById(R.id.price_form);
        startDateForm = (EditText)findViewById(R.id.start_form);
        endDateForm = (EditText)findViewById(R.id.end_form);
        createButton = (Button)findViewById(R.id.create);
        createButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    interpretForms();
                    viewListener.onClick(model);
                }
                catch (ParseException e){
                    errorMessage.setText(e.getMessage().substring(0,e.getMessage().length()-13));
                }
            }
        });
        errorMessage = (TextView)findViewById(R.id.error);
    }

    private void interpretForms() throws ParseException{
        if(nameForm.getText().toString().isEmpty()) throw new ParseException("Name is missing.",0);
        model.setName(nameForm.getText().toString());
        if(descriptionForm.getText().toString().isEmpty()) throw new ParseException("Description is missing.",0);
        model.setLongDescription(descriptionForm.getText().toString());
        MoneyParser bob = new MoneyParser();
        model.setHighestBid(bob.parse(startingBidForm.getText().toString()));
        DateParser steve = new DateParser();
        model.setStartDate(steve.parse(startDateForm.getText().toString(),DateParser.START_DATE));
        model.setEndDate(steve.parse(endDateForm.getText().toString(),DateParser.END_DATE));
        if (model.getStartDate().after(model.getEndDate())) throw new ParseException("Start Date cannot be after End Date.",0);
    }

    @Override
    public void update(Observable observable, Object data) {
        bind();
    }

    private void bind() {
    }
}
