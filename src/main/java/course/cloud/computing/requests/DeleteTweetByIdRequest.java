package course.cloud.computing.requests;

import java.sql.SQLException;

import course.cloud.computing.SocialNetworkV2.WorkItem;
import course.cloud.computing.data.SocialNetworkDataBase;

public class DeleteTweetByIdRequest implements WorkItem {

	boolean isProcessed = false;
	int userId = 0;
	int tweetId= 0;
	int response = 0;
	public DeleteTweetByIdRequest(int id, int tId)
	{
		userId = id;
		tweetId = tId;
	}
	
	@Override
	public boolean process() {
		try {
			
			response = SocialNetworkDataBase.removeTweet(userId, tweetId);
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
	
	public int getResponse(){
		return response;
	}

}
