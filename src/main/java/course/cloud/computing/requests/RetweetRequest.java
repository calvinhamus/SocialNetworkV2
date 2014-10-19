package course.cloud.computing.requests;

import java.sql.SQLException;

import course.cloud.computing.SocialNetworkV2.WorkItem;
import course.cloud.computing.classes.Tweet;
import course.cloud.computing.data.SocialNetworkDataBase;

public class RetweetRequest implements WorkItem {

	boolean isProcessed = false;
	int tweetId = 0;
	int userId = 0; 
	Tweet response = null;
	
	public RetweetRequest(int id,int uId)
	{
		tweetId = id;
		userId = uId;
	}
	@Override
	public boolean process() {
		response = new Tweet();
		try {
			response = SocialNetworkDataBase.getTweetById(tweetId);
			String originalMsg = response.getMessage();
			 response.setMessage("UserID: "+userId+" Tweet: "+ originalMsg);
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
