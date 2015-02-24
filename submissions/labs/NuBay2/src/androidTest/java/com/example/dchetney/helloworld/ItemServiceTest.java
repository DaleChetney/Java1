package com.example.dchetney.helloworld;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by dchetney on 2/24/2015.
 */
public class ItemServiceTest extends TestCase {

    public void testSearch(){
        ItemService.items.clear();

        ItemService.items.add(new ItemDetailModel(0, "A", "", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.add(new ItemDetailModel(1, "B", "", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.add(new ItemDetailModel(2, "C", "", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.add(new ItemDetailModel(3, "AB", "","", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.add(new ItemDetailModel(4, "AC", "","", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.add(new ItemDetailModel(5, "BC", "","", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.add(new ItemDetailModel(6, "Expired A", "","", 3, new Date(), new Date(System.currentTimeMillis()-(1000*60*60*24))));

        ArrayList<ItemDetailModel> a = ItemService.search("A");
        assertTrue(a.size() == 3);
        assertTrue(a.get(0).getName()=="A");
        assertTrue(a.get(1).getName()=="AB");
        assertTrue(a.get(2).getName()=="AC");
        ArrayList<ItemDetailModel> notA = ItemService.search("NOT A");
        assertTrue(notA.size() == 3);
        assertTrue(notA.get(0).getName()=="B");
        assertTrue(notA.get(1).getName()=="C");
        assertTrue(notA.get(2).getName()=="BC");
        ArrayList<ItemDetailModel> bAndC = ItemService.search("B AND C");
        assertTrue(bAndC.size() == 1);
        assertTrue(bAndC.get(0).getName()=="BC");
        ArrayList<ItemDetailModel> AorB = ItemService.search("A OR B");
        assertTrue(AorB.size() == 5);
        assertTrue(AorB.get(0).getName()=="A");
        assertTrue(AorB.get(1).getName()=="B");
        assertTrue(AorB.get(2).getName()=="AB");
        assertTrue(AorB.get(3).getName()=="AC");
        assertTrue(AorB.get(4).getName()=="BC");
        ArrayList<ItemDetailModel> compound = ItemService.search("NOT B OR B AND NOT C");
        assertTrue(compound.get(0).getName()=="A");
        assertTrue(compound.get(1).getName()=="B");
        assertTrue(compound.get(2).getName()=="C");
        assertTrue(compound.get(3).getName()=="AB");
        assertTrue(compound.get(4).getName()=="AC");
        ArrayList<ItemDetailModel> Expired = ItemService.search("Expired A");
        assertTrue(Expired.size() == 0);
    }

    public void testBid(){
        ItemService.items.clear();

        ItemService.items.add(new ItemDetailModel(0, "A", "", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.add(new ItemDetailModel(1, "B", "", "", 3, new Date(), new Date(System.currentTimeMillis()-(1000*60*60*24))));

        float currentPrice = ItemService.items.get(0).getHighestBid();
        ItemService.bid(0,1.0f);
        assertEquals(ItemService.items.get(0).getHighestBid(),currentPrice+1.0f);

        currentPrice = ItemService.items.get(1).getHighestBid();
        try{
            ItemService.bid(1,1.0f);
        }
        catch (IllegalAccessError e){
            assertEquals(ItemService.items.get(1).getHighestBid(),currentPrice);
        }
    }
}
