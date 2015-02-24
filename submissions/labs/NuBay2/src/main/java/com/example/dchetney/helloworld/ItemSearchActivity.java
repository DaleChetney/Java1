package com.example.dchetney.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;

/**
 * Created by dchetney on 1/22/2015.
 */
public class ItemSearchActivity extends ActionBarActivity {

    private ItemSearchModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemSearchView view = (ItemSearchView) View.inflate(getApplicationContext(), R.layout.search_layout, null);
        model = new ItemSearchModel(ItemService.items);
        view.setAdapter(new SearchAdapter(getApplicationContext(),model.getResults()));
        view.setOnSearchListener(searchListener);
        view.setOnSelectListener(selectListener);
        view.setViewListener(createListener);
        view.setModel(model);
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.setResults(ItemService.items);
    }

    private OnSearchListener searchListener = new OnSearchListener() {
        @Override
        public boolean onSearch(String query)
        {
            model.setResults(ItemService.search(query));
            return true;
        }
    };

    private OnSelectListener selectListener = new OnSelectListener() {
        @Override
        public void onSelect(int position) {
            Intent intent =  new Intent(getApplicationContext(),ItemDetailActivity.class);
            intent.putExtra("Item",ItemService.items.indexOf(model.getResults().get(position)));
            startActivity(intent);
        }
    };

    private ViewListener createListener = new ViewListener() {
        @Override
        public void onClick(ItemDetailModel model) {
            Intent intent =  new Intent(getApplicationContext(),ItemCreateActivity.class);
            startActivity(intent);
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
