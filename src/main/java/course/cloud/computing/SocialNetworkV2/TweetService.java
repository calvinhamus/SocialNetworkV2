package course.cloud.computing.SocialNetworkV2;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.time.StopWatch;

import com.google.gson.Gson;

import course.cloud.computing.classes.Tweet;
import course.cloud.computing.classes.Users;
import course.cloud.computing.data.SocialNetworkDataBase;
import course.cloud.computing.requests.DeleteTweetByIdRequest;
import course.cloud.computing.requests.GetPendingFollowersRequest;
import course.cloud.computing.requests.GetTweetByIdRequest;
import course.cloud.computing.requests.RetweetRequest;
import course.cloud.computing.requests.SendTweetRequest;

@Path("/tweet")
public class TweetService 
{
	Gson gson = new Gson();
	private final static String queueName = "processing-queue";
	@POST
	@Produces("application/xml")
	@Path("tweet/{msg}")
	public Response sendTweet(@Context HttpServletRequest req,@PathParam("msg") String msg) throws InterruptedException
	{
		if(msg.length() > 128)
			return Response.status(Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();//TODO FINSIH this
		HttpSession session = req.getSession();
		Tweet tweet = new Tweet();
		int id = (int) session.getAttribute("userid");
		SendTweetRequest request = new SendTweetRequest(id,msg);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		tweet = request.getResponse();
		stopwatch.stop();
		long split = stopwatch.getTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		SocialNetworkDataBase.addCode("201");
		return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(tweet)).build();
	}
	@GET
	@Produces("application/xml")
	@Path("show/{id}")
	public Response getTweetById(@Context HttpServletRequest req,@PathParam("id") int tweetId) throws InterruptedException
	{
		Tweet tweet = new Tweet();
		GetTweetByIdRequest request = new GetTweetByIdRequest(tweetId);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		tweet = request.getResponse();
		stopwatch.stop();
		long split = stopwatch.getTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		if(tweet.getId() !=0){
			SocialNetworkDataBase.addCode("201");
			return Response.status(201).entity(gson.toJson(tweet)).build();
		}
		SocialNetworkDataBase.addCode("404");
		return Response.status(Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build();
	}
	@POST
	@Produces("application/xml")
	@Path("destroy/{id}")
	public Response deleteTweetById(@Context HttpServletRequest req,@PathParam("id") int tweetId) throws InterruptedException
	{
		HttpSession session = req.getSession();
		Users users = new Users();
		int id = (int) session.getAttribute("userid");
		DeleteTweetByIdRequest request = new DeleteTweetByIdRequest(id, tweetId);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		int result = request.getResponse();
		stopwatch.stop();
		long split = stopwatch.getTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		if(result != 0){
			SocialNetworkDataBase.addCode("201");
			return Response.status(201).header("Access-Control-Allow-Origin", "*").entity("Destroyed").build();//TODO add success parameters
		}
		SocialNetworkDataBase.addCode("401");
		return Response.status(Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").entity(users).build();
	}
	@POST
	@Produces("application/xml")
	@Path("retweet/{id}")
	public Response retweetTweet(@Context HttpServletRequest req,@PathParam("id") int tweetId) throws InterruptedException
	{
		HttpSession session = req.getSession();
		Tweet tweet = new Tweet();
		int id = (int) session.getAttribute("userid");
		if(id != 0){
			RetweetRequest request = new RetweetRequest(tweetId, id);
			TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
			StopWatch stopwatch = new StopWatch();
			if(queue !=null){
				queue.add(request);
				stopwatch.start();
			}
			while (!request.isCompleted()){
				Thread.currentThread().sleep(5);
			}
			tweet = request.getResponse();
			stopwatch.stop();
			long split = stopwatch.getTime();
			SocialNetworkDataBase.addToProcessingTime(split);
			SocialNetworkDataBase.addCode("201");
			return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(tweet)).build();
		}
		SocialNetworkDataBase.addCode("401");
		return Response.status(Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
		
	}
}
