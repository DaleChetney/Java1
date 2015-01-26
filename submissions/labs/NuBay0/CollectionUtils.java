package com.example.dchetney.helloworld;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by dchetney on 1/20/2015.
 */
public class CollectionUtils {

    public <T> List<T> filter(List<T> elements,Predicate test) {
        List<T> result = new ArrayList<T>();
        for (T i : elements)
        {
            if(test.evaluate(i)){
                result.add(i);
            }
        }

        return result;
    }

    public static interface Predicate<T>{

        boolean evaluate(T obj);
    }

    public static class Search<ItemDetailModel> implements Predicate<ItemDetailModel>{

        private String query;

        public Search(String query) {
            this.query = query;
        }

        @Override
        public boolean evaluate(ItemDetailModel obj) {
            return ((com.example.dchetney.helloworld.ItemDetailModel)obj).getName().contains(query);
        }
    }

}
