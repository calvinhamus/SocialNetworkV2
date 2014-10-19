package course.cloud.computing.requests;

import java.sql.SQLException;

import course.cloud.computing.SocialNetworkV2.WorkItem;
import course.cloud.computing.classes.Tweet;
import course.cloud.computing.data.SocialNetworkDataBase;

public class SendTweetRequest implements WorkItem {

	boolean isProcessed = false;
	int userId = 0;
	String message= "";
	Tweet response = null;
	public SendTweetRequest(int id, String msg)
	{
		userId = id;
		message = msg;
	}
	
	@Override
	public boolean process() {
		response = new Tweet();
		try {
			response.setId(SocialNetworkDataBase.createNewTweet(userId, message));
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
	
	public Tweet getResponse(){
		return response;
	}
}
