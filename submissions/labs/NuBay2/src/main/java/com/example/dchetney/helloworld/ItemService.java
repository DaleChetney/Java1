package com.example.dchetney.helloworld;
import android.content.ClipData;

import java.math.BigDecimal;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by dchetney on 1/22/2015.
 */
public class ItemService {
    public static ArrayList<ItemDetailModel> items = new ArrayList<ItemDetailModel>(Arrays.asList(
            new ItemDetailModel(0,"Toaster","A Toaster","The most wickedly awesome toaster in existence.",3,new Date(System.currentTimeMillis()-(1000*60*60*24)),new Date(System.currentTimeMillis()+(1000*10))),
            new ItemDetailModel(1,"Rock Toaster","A Toaster","The most rockin toaster straight from the depths of Hell.",3,new Date(System.currentTimeMillis()+(1000*60*60*24)),new Date(System.currentTimeMillis()+(1000*60*60*48))),
            new ItemDetailModel(2,"Rocktastic Box","A Toaster","The punk-ish little toaster that only appeals to Middle-Schoolers.",3,new Date(System.currentTimeMillis()-(1000*60*60*48)),new Date(System.currentTimeMillis()-(1000*60*60*24))),
            new ItemDetailModel(3,"Toaster Box","A Toaster","This Toaster comes in an exciting box shape.",3,new Date(System.currentTimeMillis()-(1000*60*60*24)),new Date(System.currentTimeMillis()+(1000*60*60*24))),
            new ItemDetailModel(4,"Holy Roaster","A Toaster","This Toaster creates little slices of heaven!",3,new Date(System.currentTimeMillis()-(1000*60*60*24)),new Date(System.currentTimeMillis()+(1000*60*60*24))),
            new ItemDetailModel(5,"Smokester","A Toaster","Dude, like, this toaster man, woah.",3,new Date(System.currentTimeMillis()-(1000*60*60*24)),new Date(System.currentTimeMillis()+(1000*60*60*24))),
            new ItemDetailModel(6,"Bread Roaster","A Toaster","The first Toaster ever made! Makes toast without the need for peasants!",3,new Date(System.currentTimeMillis()-(1000*60*60*24)),new Date(System.currentTimeMillis()+(1000*60*60*24))),
            new ItemDetailModel(7,"Mythic Toaster","A Toaster","A Toaster of legend. Used to summon our Lord Dagon.",3,new Date(System.currentTimeMillis()-(1000*60*60*24)),new Date(System.currentTimeMillis()+(1000*60*60*24))),
            new ItemDetailModel(8,"Mythic Roaster","A Toaster","A cheap imitation of the legendary Mythic Toaster.",3,new Date(System.currentTimeMillis()-(1000*60*60*24)),new Date(System.currentTimeMillis()+(1000*60*60*24)))
    ));

    public static ArrayList<ItemDetailModel> search(String query){

        String[] splitStrings = query.split(" ");

        if(splitStrings.length==1)
            return (ArrayList) CollectionUtils.filter((ArrayList)CollectionUtils.filter(items, new CollectionUtils.Search<ItemDetailModel>(query)), new CollectionUtils.NotExpired<ItemDetailModel>());

        ArrayList<String> input = new ArrayList<String>(Arrays.asList(splitStrings));

        for (int i=0; i<input.size()-1; i++) {
            if(!ItemService.isOperator(input.get(i))&&!ItemService.isOperator(input.get(i+1))){
                input.set(i,input.get(i)+" "+input.get(i+1));
                input.remove(i+1);
            }
        }

        if(input.size()==1)
            return (ArrayList) CollectionUtils.filter((ArrayList)CollectionUtils.filter(items, new CollectionUtils.Search<ItemDetailModel>(input.get(0))), new CollectionUtils.NotExpired<ItemDetailModel>());

        ArrayList<String> queue = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();

        for (int i=0; i<input.size(); i++){
            if (ItemService.isOperator(input.get(i))){
                while(!stack.isEmpty()&&hasPrecedence(stack.peek(),input.get(i))){
                        queue.add(stack.pop());
                }
                stack.add(input.get(i));
            }
            else{
                queue.add(input.get(i));
            }
        }
        while(!stack.isEmpty()){
            queue.add(stack.pop());
        }
        input.clear();
        for(int i=queue.size()-1; i>=0; i--){
            input.add(queue.get(i));
        }

        return (ArrayList) CollectionUtils.filter(applyQuery(input, items), new CollectionUtils.NotExpired<ItemDetailModel>());

    }

    private static ArrayList<ItemDetailModel> applyQuery(ArrayList<String> query, ArrayList<ItemDetailModel> filteredItems){

        if(query.get(0).equals("NOT")) {
            return (ArrayList) CollectionUtils.filter(filteredItems, new CollectionUtils.Not<ItemDetailModel>(query.get(1)));
        }
        else if(query.get(0).equals("AND")) {
            ArrayList<String>[] split = ItemService.splitQuery(query);
            return (ArrayList) CollectionUtils.filter(filteredItems, new CollectionUtils.And<ItemDetailModel>(applyQuery(split[0],filteredItems),applyQuery(split[1],filteredItems)));
        }
        else if(query.get(0).equals("OR")) {
            ArrayList<String>[] split = ItemService.splitQuery(query);
            return (ArrayList) CollectionUtils.filter(filteredItems, new CollectionUtils.Or<ItemDetailModel>(applyQuery(split[0], filteredItems), applyQuery(split[1], filteredItems)));
        }
        else{
            return (ArrayList) CollectionUtils.filter(filteredItems, new CollectionUtils.Search<ItemDetailModel>(query.get(0)));
        }
    }

    private static ArrayList<String>[] splitQuery(ArrayList<String> query){
        ArrayList<String>[] split = new ArrayList[2];
        split[0] = new ArrayList<String>();
        split[1] = new ArrayList<String>();

        int splitIndex = 0;
        int expected = 1;
        for (int i = 1; i < query.size(); i++) {
            split[splitIndex].add(query.get(i));
            if(splitIndex==0) {
                if (query.get(i).equals("AND") || query.get(i).equals("OR")) {
                    expected++;
                }
                if(!ItemService.isOperator(query.get(i))){
                    expected--;
                    if(expected==0){
                        splitIndex=1;
                    }
                }
            }
        }
        return split;
    }

    private static boolean hasPrecedence(String token,String over){
        return (((token.equals("AND")||token.equals("NOT")) && over.equals("OR")) || (token.equals("NOT") && (over .equals("AND") || over .equals("OR"))));
    }

    private static boolean isOperator(String token){
        return (token.equals("AND") || token.equals("OR") || token.equals("NOT"));
    }

    public static ItemDetailModel bid(int id, float bidIncrease) throws IllegalAccessError{
        if (items.get(id).getEndDate().before(new Date())) throw new IllegalAccessError();
        items.get(id).setHighestBid(items.get(id).getHighestBid()+bidIncrease);
        return items.get(id);
    }
}
