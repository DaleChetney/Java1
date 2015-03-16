package NuBay;

import java.io.Serializable;

public class Packet implements Serializable {

	public Object[] objects;
	
	public Packet(Object[] objects){
		this.objects = objects;
	}
}
