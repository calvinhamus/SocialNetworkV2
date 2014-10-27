package course.cloud.computing.SocialNetworkV2;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import course.cloud.computing.classes.HTTPCode;
import course.cloud.computing.classes.HTTPCodes;
import course.cloud.computing.classes.ProcessingTime;
import course.cloud.computing.data.SocialNetworkDataBase;

@Path("/monitor")
public class MonitorService 
{
	Gson gson = new Gson();
	private final static String queueName = "processing-queue";
	
	@GET
	@Produces("application/json")
	@Path("processingtime")
	public Response getProcessingTime() throws InterruptedException
	{
		ProcessingTime time = SocialNetworkDataBase.getProcessingTime();
		SocialNetworkDataBase.addCode("201");
		return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(time)).build();
	}
	@GET
	@Produces("application/json")
	@Path("queuedepth")
	public Response getQueueDepth() throws InterruptedException
	{
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		long result = queue.getDepth();
		SocialNetworkDataBase.addCode("201");
		return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(result)).build();
	}
	@GET
	@Produces("application/json")
	@Path("qps/{resolution}")
	public Response getResolution(@PathParam("resolution") String timeFrame) throws InterruptedException
	{
		return Response.status(Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
	}
	@GET
	@Produces("application/json")
	@Path("errors/{type}")
	public Response getErrors(@PathParam("type") int errorCode) throws InterruptedException
	{
		ArrayList<HTTPCode> codes = SocialNetworkDataBase.getErrors(Integer.toString(errorCode));
		SocialNetworkDataBase.addCode("201");
		return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(gson.toJson(codes)).build();
	}
}
