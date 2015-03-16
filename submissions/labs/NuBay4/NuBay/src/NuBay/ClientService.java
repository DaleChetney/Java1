package NuBay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class ClientService implements Service {
	
	Socket s = null;
	
	public ClientService(Socket sock){
		this.s = sock;
	}

	@Override
	public HashMap<Integer, ItemDetailModel> search(String query) {
		try{
			ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
			Packet pack = new Packet(new Object[]{ServerCommand.Search,query});
			pw.flush();
			pw.writeObject(pack);
			ObjectInputStream inputStreamReader = new ObjectInputStream(s.getInputStream());
			return (HashMap<Integer, ItemDetailModel>)inputStreamReader.readObject();
		}
		catch(IOException e)
		{
			throw new ItemServiceException("The server's down!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ItemServiceException("The data you asked for couldn't be returned.");
		}
	}

	@Override
	public ItemDetailModel bid(int id, float bidIncrease){
		try{
			ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
			Packet pack = new Packet(new Object[]{ServerCommand.Bid,new Integer(id),new Float(bidIncrease)});
			pw.flush();
			pw.writeObject(pack);
			ObjectInputStream inputStreamReader = new ObjectInputStream(s.getInputStream());
			return (ItemDetailModel)inputStreamReader.readObject();
		}
		catch(IOException e)
		{
			throw new ItemServiceException("The server's down!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ItemServiceException("The data you asked for couldn't be returned.");
		}
	}

	@Override
	public ItemDetailModel update(ItemDetailModel item)
			throws ItemClientException, ItemServiceException,
			ItemNotFoundException {
		try{
			ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
			Packet pack = new Packet(new Object[]{ServerCommand.Update,item});
			pw.flush();
			pw.writeObject(pack);
			ObjectInputStream inputStreamReader = new ObjectInputStream(s.getInputStream());
			return (ItemDetailModel)inputStreamReader.readObject();
		}
		catch(IOException e)
		{
			throw new ItemServiceException("The server's down!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ItemServiceException("The data you asked for couldn't be returned.");
		}
	}

	@Override
	public void create(String nameForm, String descriptionForm,
			String startingBidForm, String startDateForm, String endDateForm)
			throws ItemClientException {
		try{
			ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
			Packet pack = new Packet(new Object[]{ServerCommand.Create,nameForm, descriptionForm,
					startingBidForm, startDateForm, endDateForm});
			pw.flush();
			pw.writeObject(pack);
		}
		catch(IOException e)
		{
			throw new ItemServiceException("The server's down!");
		}
		
	}

	@Override
	public void delete(int id) throws ItemClientException,
			ItemServiceException, ItemNotFoundException {
		try{
			ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
			Packet pack = new Packet(new Object[]{ServerCommand.Delete,new Integer(id)});
			pw.flush();
			pw.writeObject(pack);
		}
		catch(IOException e)
		{
			throw new ItemServiceException("The server's down!");
		}
	}

}
