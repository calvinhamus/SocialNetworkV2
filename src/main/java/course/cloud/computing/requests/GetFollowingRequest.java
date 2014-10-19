package course.cloud.computing.requests;

import java.sql.SQLException;

import course.cloud.computing.SocialNetworkV2.WorkItem;
import course.cloud.computing.classes.Users;
import course.cloud.computing.data.SocialNetworkDataBase;

public class GetFollowingRequest implements WorkItem {

	boolean isProcessed = false;
	int userId = 0;
	Users response = null;
	public GetFollowingRequest(int id)
	{
		userId = id;
	}
	@Override
	public boolean process() {
		response = new Users();
		try {
			response = SocialNetworkDataBase.getFollowing(userId);
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
	
	public Users getResponse(){
		return response;
	}

}
