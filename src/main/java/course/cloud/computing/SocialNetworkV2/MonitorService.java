package course.cloud.computing.SocialNetworkV2;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import course.cloud.computing.classes.ProcessingTime;
import course.cloud.computing.data.SocialNetworkDataBase;

@Path("/monitor")
public class MonitorService 
{
	private final static String queueName = "processing-queue";
	
	@GET
	@Produces("application/xml")
	@Path("processingtime")
	public Response getProcessingTime() throws InterruptedException
	{
		ProcessingTime time = SocialNetworkDataBase.getProcessingTime();
		SocialNetworkDataBase.addCode("201");
		return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(time.toString()).build();
	}
	@GET
	@Produces("application/xml")
	@Path("queuedepth")
	public Response getQueueDepth() throws InterruptedException
	{
		TaskQueue queue = ProcessingFactory.getTaskQueue(queueName);
		long result = queue.getDepth();
		SocialNetworkDataBase.addCode("201");
		return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(result).build();
	}
	@GET
	@Produces("application/xml")
	@Path("qps/{resolution}")
	public Response getResolution(@Context HttpServletRequest req,@PathParam("resolution") String timeFrame) throws InterruptedException
	{
		return Response.status(Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
	}
	@GET
	@Produces("application/xml")
	@Path("errors/{type}")
	public Response getErrors(@Context HttpServletRequest req,@PathParam("type") int errorCode) throws InterruptedException
	{
		return Response.status(201).header("Access-Control-Allow-Origin", "*").entity(SocialNetworkDataBase.getErrors(Integer.toString(errorCode))).build();
	}
}
