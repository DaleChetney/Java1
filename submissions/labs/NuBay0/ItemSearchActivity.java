package com.example.dchetney.helloworld;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by dchetney on 1/22/2015.
 */
public class ItemSearchActivity extends ActionBarActivity {

    private ItemSearchModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemSearchView view = (ItemSearchView) View.inflate(getApplicationContext(), R.layout.activity_main, null);
        model = new ItemSearchModel();
        view.setViewListener(listener);
        view.setModel(model);
        setContentView(view);
    }

    private ViewListener listener = new ViewListener() {
        @Override
        public void onClick(ItemDetailModel model)
        {
            model.setHighestBid(model.getHighestBid()+1);
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
