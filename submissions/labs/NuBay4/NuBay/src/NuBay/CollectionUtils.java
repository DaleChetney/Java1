package NuBay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

public class CollectionUtils {

    public static <K,V> HashMap<K,V> filter(HashMap<K,V> elements,Predicate test) {
    	HashMap<K,V> result = new HashMap<K,V>();
        for (Entry i : elements.entrySet())
        {
            if(test.evaluate(i.getValue())){
                result.put((K)i.getKey(),(V)i.getValue());
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
            return ((NuBay.ItemDetailModel)obj).getName().contains(query);
        }
    }

    public static class Not<ItemDetailModel> implements Predicate<ItemDetailModel>{

        private String query;

        public Not(String query) {
            this.query = query;
        }

        @Override
        public boolean evaluate(ItemDetailModel obj) {
            return !((NuBay.ItemDetailModel)obj).getName().contains(query);
        }
    }

    public static class And<ItemDetailModel> implements Predicate<ItemDetailModel>{

        private HashMap<Integer,ItemDetailModel> query1;
        private HashMap<Integer,ItemDetailModel> query2;

        public And(HashMap<Integer,ItemDetailModel> query1, HashMap<Integer,ItemDetailModel> query2) {
            this.query1 = query1;
            this.query2 = query2;
        }

        @Override
        public boolean evaluate(ItemDetailModel obj) {
            return (query1.containsValue((NuBay.ItemDetailModel)obj)&&query2.containsValue((NuBay.ItemDetailModel)obj));
        }
    }

    public static class Or<ItemDetailModel> implements Predicate<ItemDetailModel>{

        private HashMap<Integer,ItemDetailModel> query1;
        private HashMap<Integer,ItemDetailModel> query2;

        public Or(HashMap<Integer,ItemDetailModel> query1, HashMap<Integer,ItemDetailModel> query2) {
            this.query1 = query1;
            this.query2 = query2;
        }

        @Override
        public boolean evaluate(ItemDetailModel obj) {
            return (query1.containsValue((NuBay.ItemDetailModel)obj)||query2.containsValue((NuBay.ItemDetailModel)obj));
        }
    }

    public static class NotExpired<ItemDetailModel> implements Predicate<ItemDetailModel>{

        public NotExpired(){}

        @Override
        public boolean evaluate(ItemDetailModel obj) {
            return ((NuBay.ItemDetailModel) obj).getEndDate().after(new Date());
        }
    }
}
