package course.cloud.computing.requests;

import java.sql.SQLException;

import course.cloud.computing.SocialNetworkV2.WorkItem;
import course.cloud.computing.classes.User;
import course.cloud.computing.data.SocialNetworkDataBase;

public class UnfollowUserRequest implements WorkItem {

	boolean isProcessed = false;
	int userId = 0;
	int friendId = 0;
	User response = null;
	public UnfollowUserRequest(int id,int fid)
	{
		userId = id;
		friendId = fid;
	}
	@Override
	public boolean process() {
		response = new User();
		try {
			response.setId(SocialNetworkDataBase.unfollowUser(userId, friendId));
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
