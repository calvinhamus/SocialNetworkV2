package course.cloud.computing.SocialNetworkV2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.time.StopWatch;

import com.google.gson.Gson;

import course.cloud.computing.classes.User;
import course.cloud.computing.classes.Users;
import course.cloud.computing.data.SocialNetworkDataBase;
import course.cloud.computing.requests.FollowUserRequest;
import course.cloud.computing.requests.GetFollowersRequest;
import course.cloud.computing.requests.GetFollowingRequest;
import course.cloud.computing.requests.GetPendingFollowersRequest;
import course.cloud.computing.requests.GetPendingFollowingRequest;
import course.cloud.computing.requests.UnfollowUserRequest;

@Path("/friendships")
public class FollowersAndFollingService 
{
	Gson gson = new Gson();
	private final static String queueName = "processing-queue";
	
	@GET
	@Produces("application/json")
	@Path("incoming")
	public Response getPendingFollowers(@Context HttpServletRequest req) throws InterruptedException
	{
		HttpSession session = req.getSession();
		Users users = new Users();
		int id = (int) session.getAttribute("userid");
		GetPendingFollowersRequest request = new GetPendingFollowersRequest(id);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		users = request.getResponse();
		stopwatch.stop();
		long split = stopwatch.getSplitTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		SocialNetworkDataBase.addCode("201");
		return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(users)).build();
	}
	@GET
	@Produces("application/json")
	@Path("outgoing")
	public Response getPendingFollowing(@Context HttpServletRequest req) throws InterruptedException
	{
		HttpSession session = req.getSession();
		Users users = new Users();
		int id = (int) session.getAttribute("userid");
		GetPendingFollowingRequest request = new GetPendingFollowingRequest(id);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		users = request.getResponse();
		stopwatch.stop();
		long split = stopwatch.getSplitTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		SocialNetworkDataBase.addCode("201");
		return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(users)).build();
	}
	@POST
	@Produces("application/json")
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("create/{friend-id}")
	public Response followUser(@Context HttpServletRequest req,@PathParam("friend-id") int friendId) throws InterruptedException
	{
		HttpSession session = req.getSession();
		
		int id = (int) session.getAttribute("userid");
		User user = new User();
		FollowUserRequest request = new FollowUserRequest(id,friendId);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		user = request.getResponse();
		stopwatch.stop();
		long split = stopwatch.getSplitTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		if(user.getId() != 0){
			SocialNetworkDataBase.addCode("201");
			return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(user)).build();			
		}
		SocialNetworkDataBase.addCode("403");
		return Response.status(Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
	}
	@POST
	@Produces("application/json")
	@Path("destroy/{friend-id}")
	public Response unfollowUser(@Context HttpServletRequest req,@PathParam("friend-id") int friendId) throws InterruptedException
	{
		HttpSession session = req.getSession();
		int id = (int) session.getAttribute("userid");
		User user = new User();
		UnfollowUserRequest request = new UnfollowUserRequest(id,friendId);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		user = request.getResponse();
		stopwatch.stop();
		long split = stopwatch.getSplitTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		if(user.getId() != 0){
			SocialNetworkDataBase.addCode("201");
			return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(user)).build();
		}
		SocialNetworkDataBase.addCode("403");
		return Response.status(Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
	}
	@GET
	@Produces("application/json")
	@Path("friends/list")
	public Response getFollowing(@Context HttpServletRequest req) throws InterruptedException
	{
		HttpSession session = req.getSession();
		int id = (int) session.getAttribute("userid");
		Users users = new Users();
		GetFollowingRequest request = new GetFollowingRequest(id);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		users = request.getResponse();
		stopwatch.stop();
		long split = stopwatch.getSplitTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		if(!users.getUserList().isEmpty()){
			SocialNetworkDataBase.addCode("201");
			return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(users)).build();
		}
		SocialNetworkDataBase.addCode("403");
		return Response.status(Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
	}
	@GET
	@Produces("application/json")
	@Path("followers/list")
	public Response getFollowers(@Context HttpServletRequest req) throws InterruptedException
	{
		HttpSession session = req.getSession();
		int id = (int) session.getAttribute("userid");
		Users users = new Users();
		GetFollowersRequest request = new GetFollowersRequest(id);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		users = request.getResponse();
		stopwatch.stop();
		long split = stopwatch.getSplitTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		if(!users.getUserList().isEmpty()){
			SocialNetworkDataBase.addCode("201");
			return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(users)).build();
		}
		SocialNetworkDataBase.addCode("403");
		return Response.status(Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
	}
}
