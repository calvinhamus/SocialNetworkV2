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
		try {
			response = new User();
			response.setId(SocialNetworkDataBase.insertUser(userName, "a@a.com", "password"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
