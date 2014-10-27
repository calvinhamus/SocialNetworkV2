package course.cloud.computing.SocialNetworkV2;

import java.sql.SQLException;

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
import course.cloud.computing.data.SocialNetworkDataBase;
import course.cloud.computing.requests.CreateUserRequest;
import course.cloud.computing.requests.GetUserByNameRequest;
import course.cloud.computing.requests.LoginRequest;

@Path("/users")
public class UsersService //implements IUsersService 
{
	Gson gson = new Gson();
	private final static String queueName = "processing-queue";
	@POST
	@Produces("application/json")
	@Path("create/{user-name}")
	public Response createUser(@PathParam("user-name") String userName) throws InterruptedException, SQLException
	{
		User newUser = new User();
		CreateUserRequest request = new CreateUserRequest(userName);
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		StopWatch stopwatch = new StopWatch();
		if(queue !=null){
			queue.add(request);
			stopwatch.start();
		}
		while (!request.isCompleted()){
			Thread.currentThread().sleep(5);
		}
		newUser = request.getResponse();
		SocialNetworkDataBase.closeConn();
		stopwatch.stop();
		long split = stopwatch.getTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		if(newUser.getId() !=0){	
			SocialNetworkDataBase.addCode("201");
			
			return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(newUser)).build();
		}
		SocialNetworkDataBase.addCode("404");
		return Response.status(Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Produces("application/json")
	@Path("{users-name}")
	public Response getUserByName(@PathParam("users-name") String userName) throws InterruptedException, SQLException 
	//public Response getUserByName(String userName) 
	{
		User user = new User();
		GetUserByNameRequest request = new GetUserByNameRequest(userName);
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
		SocialNetworkDataBase.closeConn();
		stopwatch.stop();
		long split = stopwatch.getTime();
		
		SocialNetworkDataBase.addToProcessingTime(split);
		if(user.getId() !=0){
			SocialNetworkDataBase.addCode("201");
			return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(user)).build();	
		}
		SocialNetworkDataBase.addCode("404");
		return Response.status(Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Produces("application/json")
	//@Consumes(MediaType.TEXT_PLAIN)
	@Path("login/{user-id}")
	public Response loginUser(@Context HttpServletRequest req,@PathParam("user-id") int userID) throws InterruptedException, SQLException 
	{
		User user = new User();
		Integer id = null;
		LoginRequest request = new LoginRequest(userID);
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
		SocialNetworkDataBase.closeConn();
		stopwatch.stop();
		long split = stopwatch.getTime();
		SocialNetworkDataBase.addToProcessingTime(split);
		user = request.getResponse();
			if(user.getId()!=0){
				try {
						if (req == null) {
							System.out.println("Null request in context");
						}
						HttpSession session = req.getSession();
						id = (Integer) session.getAttribute("userid");
						if (id == null) {
							id = userID;
							session.setAttribute("userid", id);
							SocialNetworkDataBase.addCode("201");
							return Response.status(Status.ACCEPTED).header("Access-Control-Allow-Origin", "*").build();
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			SocialNetworkDataBase.addCode("404");
		return Response.status(Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build();

	}


}
