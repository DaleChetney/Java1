package NuBay;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server{
	public static void main(String args[])
	{
		try (ServerSocket ss = new ServerSocket(8080))
		{
			while(true){
				Socket s = ss.accept();
				System.out.println("Connected, waiting for requests.");
				try{
					while(s.isConnected()){
					
						ObjectInputStream inputStreamReader = new ObjectInputStream(s.getInputStream());
						Packet pack;
						try {
							pack = (Packet)inputStreamReader.readObject();
						} catch (ClassNotFoundException e) {
							throw new ItemServiceException("The data couldn't reach the server.");
						}
						if(pack.objects[0]==ServerCommand.Search){
							HashMap<Integer,ItemDetailModel> results = ItemService.getInstance().search((String)pack.objects[1]);
							System.out.println("Recieved request to search for "+(String)pack.objects[1]);
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.flush();
							pw.writeObject(results);
						}
						else if(pack.objects[0]==ServerCommand.Bid){
							ItemDetailModel model = ItemService.getInstance().bid((Integer)pack.objects[1], (Float)pack.objects[2]);
							System.out.println("Recieved request to bid on "+ItemService.getInstance().items.get((Integer)pack.objects[1]).getName());
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.flush();
							pw.writeObject(model);
						}
						else if(pack.objects[0]==ServerCommand.Update){
							System.out.print("Recieved request to change "+ItemService.getInstance().items.get(((ItemDetailModel)pack.objects[1]).getId()).getName());
							ItemDetailModel model = ItemService.getInstance().update((ItemDetailModel)pack.objects[1]);
							System.out.println(" to " + model.getName());
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.flush();
							pw.writeObject(model);
						}
						else if(pack.objects[0]==ServerCommand.Create){
							ItemService.getInstance().create((String)pack.objects[1], (String)pack.objects[2], (String)pack.objects[3], (String)pack.objects[4], (String)pack.objects[5]);
							System.out.println("Recieved request to create " + (String)pack.objects[1]);
						}
						else if(pack.objects[0]==ServerCommand.Delete){
							System.out.println("Recieved request to delete "+ItemService.getInstance().items.get((Integer)pack.objects[1]).getName());
							ItemService.getInstance().delete((Integer)pack.objects[1]);
						}
					}
				}catch(EOFException e)
				{
					System.out.println("Waiting for new Client.");
				}
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
