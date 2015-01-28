package com.example.dchetney.helloworld;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.math.BigDecimal;


public class ItemDetailActivity extends ActionBarActivity {

    private ItemDetailModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemDetailView view = (ItemDetailView)View.inflate(getApplicationContext(),R.layout.activity_main,null);
        Bundle extras = getIntent().getExtras();
        model = ItemService.items.get(extras.getInt("Item"));
        view.setViewListener(listener);
        view.setModel(model);
        setContentView(view);
    }

    private ViewListener listener = new ViewListener() {
        @Override
        public void onClick(ItemDetailModel model)
        {
            model = ItemService.bid(model.getId(),1);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
