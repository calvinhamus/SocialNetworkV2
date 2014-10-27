package course.cloud.computing.requests;

import java.sql.SQLException;

import course.cloud.computing.SocialNetworkV2.WorkItem;
import course.cloud.computing.classes.User;
import course.cloud.computing.data.SocialNetworkDataBase;

public class CreateUserRequest implements WorkItem {

	boolean isProcessed = false;
	String userName = null;
	User response = null;
	public CreateUserRequest(String un)
	{
		userName = un;
	}

	@Override
	public boolean process() {
		response = new User();
		try {
			response.setId(SocialNetworkDataBase.insertUser(userName, "a@a.com", "password"));
			response.setName(userName);
		} catch (SQLException e) {
			response.setMessage(e.toString());
			// TODO Auto-generated catch block
			
		}
		isProcessed = true;
		return isProcessed;
	}
	public boolean isCompleted(){
		return isProcessed;
	}
	
	public User getResponse(){
		return response;
	}

}
