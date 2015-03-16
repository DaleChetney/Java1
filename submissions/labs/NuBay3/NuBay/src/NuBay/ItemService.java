package NuBay;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class ItemService {
	public static String localFile;
	
    public static HashMap<Integer,ItemDetailModel> items = new HashMap<Integer,ItemDetailModel>();

    public static void setToFile(String filename) throws ItemServiceException{
    	items.clear();
    	
    	InputStream is;
    	try {
			is = new FileInputStream(new File(filename));
		} catch (FileNotFoundException e) {
			throw new ItemServiceException("File not found.");
		}
    	BufferedReader br = new BufferedReader(new InputStreamReader(is));
    	String input;
    	
    	try {
			input = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			throw new ItemServiceException("Unusable data in file.");
		}
    	
    	while(input!=null){
    		String[] params = input.split("__");
    		if(params[0].equals("delete")){
    			deleteFromFile(Integer.parseInt(params[1]));
    		}
    		else if(params[0].equals("update")){
    			try {
					updateFromFile(params[1],params[2],params[3],params[4],params[5],params[6]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					throw new ItemServiceException("File contains invalid data.");
		    	} catch (IndexOutOfBoundsException e) {
					// TODO Auto-generated catch block
					throw new ItemServiceException("File contains invalid data.");
				}
    		}
    		else{
	    		try {
					createFromFile(params[0],params[1],params[2],params[3],params[4]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					throw new ItemServiceException("File contains invalid data.");
		    	} catch (IndexOutOfBoundsException e) {
					// TODO Auto-generated catch block
					throw new ItemServiceException("File contains invalid data.");
				}
    		}
    		try {
				input = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new ItemServiceException("Unusable data in file.");
			}
    	}
    	
    	localFile=filename;
    }
    
    public static void updateFile(String command,ItemDetailModel item) throws IOException{
    	try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(localFile,true)))){
	    	StringBuilder input = new StringBuilder();
	    	if(command.equals("create")){
	    		MoneyParser mon = new MoneyParser();
		    	DateParser dat = new DateParser();
		    	input.append(item.getName());
		    	input.append("__");
		    	input.append(item.getLongDescription());
		    	input.append("__");
		    	input.append(mon.fileFormat(item.getHighestBid()));
		    	input.append("__");
		    	input.append(dat.fileFormat(item.getStartDate()));
		    	input.append("__");
		    	input.append(dat.fileFormat(item.getEndDate()));
		    	pw.println(input.toString());
	    	}
	    	else if(command.equals("delete")){
	    		input.append(command);
		    	input.append("__");
		    	input.append(item.getId());
		    	pw.println(input.toString());
	    	}
	    	else if(command.equals("update")){
	    		input.append(command);
		    	input.append("__");
		    	input.append(item.getId());
		    	input.append("__");
		    	MoneyParser mon = new MoneyParser();
		    	DateParser dat = new DateParser();
		    	input.append(item.getName());
		    	input.append("__");
		    	input.append(item.getLongDescription());
		    	input.append("__");
		    	input.append(mon.fileFormat(item.getHighestBid()));
		    	input.append("__");
		    	input.append(dat.fileFormat(item.getStartDate()));
		    	input.append("__");
		    	input.append(dat.fileFormat(item.getEndDate()));
		    	pw.println(input.toString());
	    	}
	    	
    	}catch(IOException e){
    		e.printStackTrace();
    	};
    }
    
    public static void clearFile(String filename) throws ItemServiceException{
    	PrintWriter pw;
		try {
			pw = new PrintWriter(filename);
	    	pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ItemServiceException("File not found.");
		}
    }
    
    public static HashMap<Integer,ItemDetailModel> search(String query){

        String[] splitStrings = query.split(" ");

        if(splitStrings.length==1)
            return CollectionUtils.filter(CollectionUtils.filter(items, new CollectionUtils.Search<ItemDetailModel>(query)), new CollectionUtils.NotExpired<ItemDetailModel>());

        ArrayList<String> input = new ArrayList<String>(Arrays.asList(splitStrings));

        for (int i=0; i<input.size()-1; i++) {
            if(!ItemService.isOperator(input.get(i))&&!ItemService.isOperator(input.get(i+1))){
                input.set(i,input.get(i)+" "+input.get(i+1));
                input.remove(i+1);
            }
        }

        if(input.size()==1)
            return CollectionUtils.filter(CollectionUtils.filter(items, new CollectionUtils.Search<ItemDetailModel>(input.get(0))), new CollectionUtils.NotExpired<ItemDetailModel>());

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

        return CollectionUtils.filter(applyQuery(input, items), new CollectionUtils.NotExpired<ItemDetailModel>());

    }

    private static HashMap<Integer,ItemDetailModel> applyQuery(ArrayList<String> query, HashMap<Integer,ItemDetailModel> filteredItems){

        if(query.get(0).equals("NOT")) {
            return CollectionUtils.filter(filteredItems, new CollectionUtils.Not<ItemDetailModel>(query.get(1)));
        }
        else if(query.get(0).equals("AND")) {
            ArrayList<String>[] split = ItemService.splitQuery(query);
            return CollectionUtils.filter(filteredItems, new CollectionUtils.And<ItemDetailModel>(applyQuery(split[0],filteredItems),applyQuery(split[1],filteredItems)));
        }
        else if(query.get(0).equals("OR")) {
            ArrayList<String>[] split = ItemService.splitQuery(query);
            return CollectionUtils.filter(filteredItems, new CollectionUtils.Or<ItemDetailModel>(applyQuery(split[0], filteredItems), applyQuery(split[1], filteredItems)));
        }
        else{
            return CollectionUtils.filter(filteredItems, new CollectionUtils.Search<ItemDetailModel>(query.get(0)));
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

    public static ItemDetailModel bid(int id, float bidIncrease) throws ItemClientException{
        if (items.get(id).getEndDate().before(new Date())) throw new ItemClientException("That item has expired.");
        items.get(id).setHighestBid(items.get(id).getHighestBid()+bidIncrease);
        return items.get(id);
    }

    public static ItemDetailModel update(ItemDetailModel item) throws ItemClientException, ItemServiceException, ItemNotFoundException{
    	if(item==null||item.getName().isEmpty()||item.getLongDescription().isEmpty()
    			||item.getHighestBid()<0||item.getStartDate().after(item.getEndDate()))
    		throw new ItemClientException("Invalid item data.");
    	if(items.get(item.getId())==null)throw new ItemNotFoundException();
        items.put(item.getId(),item);
        try {
			updateFile("update",item);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ItemServiceException("Data file is missing.");
		}
        return item;
    }
    
    public static void updateFromFile(String id,String nameForm,String descriptionForm,String startingBidForm,String startDateForm,String endDateForm) throws ParseException{
    	ItemDetailModel model = new ItemDetailModel();
    	model.setId(Integer.parseInt(id));
    	if(nameForm.isEmpty()) throw new ParseException("Name is missing.",0);
        model.setName(nameForm);
        if(descriptionForm.isEmpty()) throw new ParseException("Description is missing.",0);
        model.setLongDescription(descriptionForm);
        MoneyParser bob = new MoneyParser();
        model.setHighestBid(bob.parse(startingBidForm));
        DateParser steve = new DateParser();
        model.setStartDate(steve.parse(startDateForm,DateParser.START_DATE));
        model.setEndDate(steve.parse(endDateForm,DateParser.END_DATE));
        if (model.getStartDate().after(model.getEndDate())) throw new ParseException("Start Date cannot be after End Date.",0);
        
        items.put(model.getId(), model);
    }
    
    public static void create (String nameForm,String descriptionForm,String startingBidForm,String startDateForm,String endDateForm) throws ItemClientException {
    	ItemDetailModel model = new ItemDetailModel();
    	model.setId(items.values().size());
    	if(nameForm.isEmpty()) throw new ItemClientException("Name is missing.");
        model.setName(nameForm);
        if(descriptionForm.isEmpty()) throw new ItemClientException("Description is missing.");
        model.setLongDescription(descriptionForm);
        MoneyParser bob = new MoneyParser();
        try {
			model.setHighestBid(bob.parse(startingBidForm));
		
			DateParser steve = new DateParser();
			model.setStartDate(steve.parse(startDateForm,DateParser.START_DATE));
			model.setEndDate(steve.parse(endDateForm,DateParser.END_DATE));
        } catch (ParseException e1) {
			// TODO Auto-generated catch block
        	throw new ItemClientException("Invalid date.");
		}
        if (model.getStartDate().after(model.getEndDate())) throw new ItemClientException("Start Date cannot be after End Date.");
        
        items.put(model.getId(), model);
        
        try {
			updateFile("create",model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void createFromFile (String nameForm,String descriptionForm,String startingBidForm,String startDateForm,String endDateForm) throws ParseException{
    	ItemDetailModel model = new ItemDetailModel();
    	model.setId(items.values().size());
    	if(nameForm.isEmpty()) throw new ParseException("Name is missing.",0);
        model.setName(nameForm);
        if(descriptionForm.isEmpty()) throw new ParseException("Description is missing.",0);
        model.setLongDescription(descriptionForm);
        MoneyParser bob = new MoneyParser();
        model.setHighestBid(bob.parse(startingBidForm));
        DateParser steve = new DateParser();
        model.setStartDate(steve.parse(startDateForm,DateParser.START_DATE));
        model.setEndDate(steve.parse(endDateForm,DateParser.END_DATE));
        if (model.getStartDate().after(model.getEndDate())) throw new ParseException("Start Date cannot be after End Date.",0);
        
        items.put(model.getId(), model);
    }

    public static void delete(int id) throws ItemClientException, ItemServiceException, ItemNotFoundException{
    	try {
			updateFile("delete",items.get(id));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ItemServiceException("Data file is missing.");
		}
    	if(id<0)throw new ItemClientException("Invalid id.");
    	if(items.get(id)==null)throw new ItemNotFoundException();
        items.remove(id);
    }
    
    public static void deleteFromFile(int id){
    	
        items.remove(id);
    }
}
