package NuBay;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;


public class Client
{
	public static void main(String args[])
	{
		Socket s = null;

		try
		{
			s = new Socket("localhost", 8080);
			ClientService cs = new ClientService(s);
			cs.bid(0, 1);
			cs.create("Big Toaster", "It's so big.", "6.0", "3/2/15", "3/20/15");
			HashMap <Integer,ItemDetailModel> search = cs.search("Toaster AND NOT Holy");
			ItemDetailModel newModel = null;
			for(ItemDetailModel i : search.values()){
				if(i.getName().equals("Big Toaster"))newModel = new ItemDetailModel(i.getId(),"Small Toaster", "It's so small.",i.getHighestBid(), i.getStartDate(), i.getEndDate());
			}
			
			newModel = cs.update(newModel);
			cs.delete(newModel.getId());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(s != null)
			{
				try
				{
					s.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}

			}
		}
	}
}
