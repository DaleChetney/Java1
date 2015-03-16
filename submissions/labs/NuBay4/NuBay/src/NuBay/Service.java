package NuBay;

import java.io.IOException;
import java.util.HashMap;

public interface Service {
	
	public HashMap<Integer,ItemDetailModel> search(String query);
	
	public ItemDetailModel bid(int id, float bidIncrease) throws ItemClientException;
	
	public ItemDetailModel update(ItemDetailModel item) throws ItemClientException, ItemServiceException, ItemNotFoundException;
	
	public void create (String nameForm,String descriptionForm,String startingBidForm,String startDateForm,String endDateForm) throws ItemClientException;
	
	public void delete(int id) throws ItemClientException, ItemServiceException, ItemNotFoundException;
}
