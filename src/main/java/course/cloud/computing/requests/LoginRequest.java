package course.cloud.computing.requests;

import java.sql.SQLException;

import course.cloud.computing.SocialNetworkV2.WorkItem;
import course.cloud.computing.classes.User;
import course.cloud.computing.data.SocialNetworkDataBase;

public class LoginRequest implements WorkItem {

	boolean isProcessed = false;
	int userId = 0;
	User response = null;
	public LoginRequest(int id)
	{
		userId = id;
	}
	@Override
	public boolean process() {
		response = new User();
		try {
			response.setId(SocialNetworkDataBase.getUserById(userId));
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
