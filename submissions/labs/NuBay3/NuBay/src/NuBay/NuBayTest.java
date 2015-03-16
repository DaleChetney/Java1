package NuBay;

import junit.framework.TestCase;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NuBayTest extends TestCase {

    public void testSearch(){
        ItemService.items.clear();

        ItemService.items.put(0,new ItemDetailModel(0,"A", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.put(1,new ItemDetailModel(1,"B", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.put(2,new ItemDetailModel(2,"C", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.put(3,new ItemDetailModel(3,"AB", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.put(4,new ItemDetailModel(4,"AC", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.put(5,new ItemDetailModel(5,"BC", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.put(6,new ItemDetailModel(6,"Expired A", "", 3, new Date(), new Date(System.currentTimeMillis()-(1000*60*60*24))));

        List<ItemDetailModel> a = new ArrayList<ItemDetailModel>(ItemService.search("A").values());
        assertTrue(a.size() == 3);
        assertTrue(a.get(0).getName()=="A");
        assertTrue(a.get(1).getName()=="AB");
        assertTrue(a.get(2).getName()=="AC");
        List<ItemDetailModel> notA = new ArrayList<ItemDetailModel>(ItemService.search("NOT A").values());
        assertTrue(notA.size() == 3);
        assertTrue(notA.get(0).getName()=="B");
        assertTrue(notA.get(1).getName()=="C");
        assertTrue(notA.get(2).getName()=="BC");
        List<ItemDetailModel> bAndC = new ArrayList<ItemDetailModel>(ItemService.search("B AND C").values());
        assertTrue(bAndC.size() == 1);
        assertTrue(bAndC.get(0).getName()=="BC");
        List<ItemDetailModel> AorB = new ArrayList<ItemDetailModel>(ItemService.search("A OR B").values());
        assertTrue(AorB.size() == 5);
        assertTrue(AorB.get(0).getName()=="A");
        assertTrue(AorB.get(1).getName()=="B");
        assertTrue(AorB.get(2).getName()=="AB");
        assertTrue(AorB.get(3).getName()=="AC");
        assertTrue(AorB.get(4).getName()=="BC");
        List<ItemDetailModel> compound = new ArrayList<ItemDetailModel>(ItemService.search("NOT B OR B AND NOT C").values());
        assertTrue(compound.get(0).getName()=="A");
        assertTrue(compound.get(1).getName()=="B");
        assertTrue(compound.get(2).getName()=="C");
        assertTrue(compound.get(3).getName()=="AB");
        assertTrue(compound.get(4).getName()=="AC");
        List<ItemDetailModel> Expired = new ArrayList<ItemDetailModel>(ItemService.search("Expired A").values());
        assertTrue(Expired.size() == 0);
    }

    public void testBid(){
        ItemService.items.clear();

        ItemService.items.put(0,new ItemDetailModel(0,"A", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.put(1,new ItemDetailModel(1,"B", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));

        float currentPrice = ItemService.items.get(0).getHighestBid();
        try {
			ItemService.bid(0,1.0f);
		} catch (ItemClientException e1) {
			// TODO Auto-generated catch block
			fail();
		}
        assertEquals(ItemService.items.get(0).getHighestBid(),currentPrice+1.0f);

        currentPrice = ItemService.items.get(1).getHighestBid();
        try{
            ItemService.bid(1,1.0f);
        }
        catch (ItemClientException e){
            assertEquals(ItemService.items.get(1).getHighestBid(),currentPrice);
        }
    }
    
    public void testDateParse(){
        DateParser parser = new DateParser();
        try {
            parser.parse("word",DateParser.START_DATE);
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("Invalid date format."));
        }
        try {
            parser.parse("30 15 15",DateParser.START_DATE);
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("That's not a real month."));
        }
        try {
            parser.parse("2 30 15", DateParser.END_DATE);
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("That's not a real day."));
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM dd yy");
            assertEquals(parser.parse("01.23.2015",DateParser.END_DATE),sdf.parse("01 23 15"));
            assertEquals(parser.parse("1/23/2015",DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("01-23-2015",DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("1-2015",DateParser.END_DATE), sdf.parse("01 01 15"));
            assertEquals(parser.parse("Jan 23 2015",DateParser.END_DATE),sdf.parse("01 23 15"));
            assertEquals(parser.parse("Jan 23, 2015",DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("January 23, 2015",DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("January 23 2015", DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("",DateParser.START_DATE), new Date());
            assertEquals(parser.parse("",DateParser.END_DATE), new Date(System.currentTimeMillis()+(1000*60*60*24*7)));
        }
        catch (ParseException e){
            fail();
        }
    }
    public void testDateFormat(){
        DateParser parser = new DateParser();
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yy");
        Date date = null;
        try {
            date = sdf.parse("01 23 15");
        }
        catch (ParseException e){
            fail();
        }
        assertEquals(parser.format(date),"Bidding opens on Jan 23, 2015");
        assertEquals(parser.formatTimeUntil(new Date(System.currentTimeMillis()+(1000*60*60*24*7))),"Bidding closes in 7 days, ");
    }
    
    public void testMoneyParse() {
        MoneyParser parser = new MoneyParser();
        try {
            parser.parse("word");
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("No numbers in your price."));
        }
        try {
            parser.parse("-1");
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("No negatives allowed."));
        }
        try {
            assertEquals(parser.parse(""), 0.01f);
            assertEquals(parser.parse("1"), 1.0f);
            assertEquals(parser.parse("1.00"), 1.0f);
            assertEquals(parser.parse("$1.00"), 1.0f);
            assertEquals(parser.parse("$1"), 1.0f);
            assertEquals(parser.parse("$1,000"), 1.0f);
            assertEquals(parser.parse("$1,00"), 1.0f);
            assertEquals(parser.parse("1.0"), 1.0f);
        }
        catch (ParseException e){
            fail();
        }
    }

    public void testMoneyFormat() {
        MoneyParser parser = new MoneyParser();
        assertEquals(parser.format(1),"$1.00");
    }
    
    public void testCreate(){
        ItemService.items.clear();
        ItemService.localFile = "emptyFile.txt";
        
        try {
			ItemService.create("A", "descriptionA", "3", "1/1/15", "1/2/15");
			ItemService.create("B", "descriptionB", "5", "1/3/15", "1/4/15");
		
        
        SimpleDateFormat sdf = new SimpleDateFormat("M/D/yy");
        
        assertEquals(ItemService.items.get(0).getName(),"A");
        assertEquals(ItemService.items.get(0).getLongDescription(),"descriptionA");
        assertEquals(ItemService.items.get(0).getHighestBid(),3.0f);
        assertEquals(ItemService.items.get(0).getStartDate(),sdf.parse("1/1/15"));
        assertEquals(ItemService.items.get(0).getEndDate(),sdf.parse("1/2/15"));
        
        assertEquals(ItemService.items.get(1).getName(),"B");
        assertEquals(ItemService.items.get(1).getLongDescription(),"descriptionB");
        assertEquals(ItemService.items.get(1).getHighestBid(),5.0f);
        assertEquals(ItemService.items.get(1).getStartDate(),sdf.parse("1/3/15"));
        assertEquals(ItemService.items.get(1).getEndDate(),sdf.parse("1/4/15"));
        
        } catch (ItemClientException e) {
			// TODO Auto-generated catch block
			fail();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			fail();
		}
    }
    
    public void testUpdate(){
        ItemService.items.clear();
        ItemService.localFile = "emptyFile.txt";

        ItemService.items.put(0,new ItemDetailModel(0,"A", "descriptionA", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.put(1,new ItemDetailModel(1,"B", "descriptionB", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));

        try{
        	ItemService.update(new ItemDetailModel(2,"C", "descriptionC", 5, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        } catch (ItemNotFoundException e){
        	
        } catch (ItemClientException e) {
			fail();
		} catch (ItemServiceException e) {
			fail();
		} 
        try{
        	ItemService.update(new ItemDetailModel(-1,"C", "", 5, new Date(), new Date(System.currentTimeMillis()-(1000*60*60*24))));
        }catch (ItemClientException e) {
        	assertEquals(e.getMessage(),"Invalid item data.");
		} catch (ItemServiceException e) {
			fail();
		} catch (ItemNotFoundException e) {
			// TODO Auto-generated catch block
			fail();
		}
        

    	try {
			ItemService.update(new ItemDetailModel(0,"C", "descriptionC", 5, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
		} catch (ItemClientException | ItemServiceException
				| ItemNotFoundException e) {
			fail();
		}
        
        assertTrue(ItemService.items.get(0).getName()=="C");
        
        try {
			ItemService.clearFile("emptyFile.txt");
		} catch (ItemServiceException e) {
			assertEquals(e.getMessage(),"File not found.");
		}
    }
    
    public void testDelete(){
        ItemService.items.clear();
        ItemService.localFile = "emptyFile.txt";

        ItemService.items.put(0,new ItemDetailModel(0,"A", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
        ItemService.items.put(1,new ItemDetailModel(1,"B", "", 3, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));

        try {
			ItemService.delete(0);
		} catch (ItemClientException e) {
			assertEquals(e.getMessage(),"Invalid id.");
		} catch (ItemServiceException e) {
			assertEquals(e.getMessage(),"Data file is missing.");
		} catch (ItemNotFoundException e) {
			
		}
        
        assertEquals(ItemService.items.values().size(),1);
        
        try {
			ItemService.clearFile("emptyFile.txt");
		} catch (ItemServiceException e) {
			assertEquals(e.getMessage(),"File not found.");
		}
    }
    
    public void testRead(){
    	
    	try {
			ItemService.setToFile("esadft");
		} catch (ItemServiceException e) {
			// TODO Auto-generated catch block
			assertEquals(e.getMessage(),"File not found.");
		}
    	
    	try {
			ItemService.setToFile("invalidFile.txt");
		} catch (ItemServiceException e) {
			// TODO Auto-generated catch block
			assertEquals(e.getMessage(),"File contains invalid data.");
		}
    	
    	try {
			ItemService.setToFile("items.txt");
		} catch (ItemServiceException e) {
			// TODO Auto-generated catch block
			fail();
		}
    	assertEquals(ItemService.items.size(),9);
    	assertEquals(ItemService.items.get(0).getName(),"Toaster");
    }
    
public void testWrite(){
    	ItemService.localFile = "testfile.txt";
    	try {
			ItemService.create("A", "descriptionA", "3", "1/1/15", "1/2/15");
			ItemService.create("B", "descriptionB", "5", "1/3/15", "1/4/15");
			ItemService.delete(0);
			ItemService.update(new ItemDetailModel(1,"C", "descriptionC", 5, new Date(), new Date(System.currentTimeMillis()+(1000*60*60*24))));
			
			ItemService.items.clear();
			
			ItemService.setToFile(ItemService.localFile);
		} catch (ItemClientException e) {
			// TODO Auto-generated catch block
			fail();
		} catch (ItemServiceException e) {
			// TODO Auto-generated catch block
			fail();
		} catch (ItemNotFoundException e) {
			// TODO Auto-generated catch block
			fail();
		}
    	assertEquals(ItemService.items.size(),1);
    	assertEquals(ItemService.items.get(1).getName(),"C");
    	try {
			ItemService.clearFile("testfile.txt");
		} catch (ItemServiceException e) {
			assertEquals(e.getMessage(),"File not found.");
		}
    }
}
