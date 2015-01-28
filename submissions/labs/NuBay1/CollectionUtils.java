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

    public static <T> ArrayList<T> filter(ArrayList<T> elements,Predicate test) {
        ArrayList<T> result = new ArrayList<T>();
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

    public static class Not<ItemDetailModel> implements Predicate<ItemDetailModel>{

        private String query;

        public Not(String query) {
            this.query = query;
        }

        @Override
        public boolean evaluate(ItemDetailModel obj) {
            return !((com.example.dchetney.helloworld.ItemDetailModel)obj).getName().contains(query);
        }
    }

    public static class And<ItemDetailModel> implements Predicate<ItemDetailModel>{

        private ArrayList<ItemDetailModel> query1;
        private ArrayList<ItemDetailModel> query2;

        public And(ArrayList<ItemDetailModel> query1, ArrayList<ItemDetailModel> query2) {
            this.query1 = query1;
            this.query2 = query2;
        }

        @Override
        public boolean evaluate(ItemDetailModel obj) {
            return (query1.contains((com.example.dchetney.helloworld.ItemDetailModel)obj)&&query2.contains((com.example.dchetney.helloworld.ItemDetailModel)obj));
        }
    }

    public static class Or<ItemDetailModel> implements Predicate<ItemDetailModel>{

        private ArrayList<ItemDetailModel> query1;
        private ArrayList<ItemDetailModel> query2;

        public Or(ArrayList<ItemDetailModel> query1, ArrayList<ItemDetailModel> query2) {
            this.query1 = query1;
            this.query2 = query2;
        }

        @Override
        public boolean evaluate(ItemDetailModel obj) {
            return (query1.contains((com.example.dchetney.helloworld.ItemDetailModel)obj)||query2.contains((com.example.dchetney.helloworld.ItemDetailModel)obj));
        }
    }
}
