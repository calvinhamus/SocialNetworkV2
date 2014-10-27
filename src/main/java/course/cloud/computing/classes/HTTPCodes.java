package course.cloud.computing.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class HTTPCodes 
{
	private ArrayList<HTTPCode> codesList = new ArrayList<HTTPCode>();
	
	public void addCode(HTTPCode code)
	{
		codesList.add(code);
		Collections.sort(codesList);
	}
	public ArrayList<HTTPCode> getErrors(String type)
	{
		return getCodes(type);	
	}
	public ArrayList<HTTPCode> getSuccess(String resolution)
	{
		Date d = new Date();
		switch (resolution) {
		case "minute":
			return getLastMinuteMessages(d);
			//break;
		case "hour":
			return getLastHourMessages(d);	
					//break;
		case "day":
			return getLastDayMessages(d);
			//break;
		case "month":
			return getLastMonthMessages(d);
			//break;
		default:
			break;
		}
		return null;
		
	}
	private ArrayList<HTTPCode> getLastMonthMessages(Date d) {
		ArrayList<HTTPCode> returnList = new ArrayList<HTTPCode>();
		int count = 0;
		for(HTTPCode code : codesList)
		{
			if (count <=100 &&(code.getTime().getMonth() <= d.getMonth() && code.getTime().getMonth() >= d.getMonth()-1))
					{
						returnList.add(code);
					}
			count++;
		}
		return returnList;
	}
	private ArrayList<HTTPCode> getLastDayMessages(Date d) {
		ArrayList<HTTPCode> returnList = new ArrayList<HTTPCode>();
		int count = 0;
		for(HTTPCode code : codesList)
		{
			if (count <=100 &&code.getTime().getMonth() == d.getMonth() && 
					(code.getTime().getDay() <= d.getDay() && code.getTime().getDay() >= d.getDay()-1))
					{
						returnList.add(code);
					}
			count++;
		}
		return returnList;
	}
	private ArrayList<HTTPCode> getLastHourMessages(Date d) 
	{
		ArrayList<HTTPCode> returnList = new ArrayList<HTTPCode>();
		int count = 0;
		for(HTTPCode code : codesList)
		{
			if (count <=100 &&code.getTime().getMonth() == d.getMonth() && code.getTime().getDay() == d.getDay() && 
					(code.getTime().getHours() <= d.getHours() && code.getTime().getHours() >= d.getHours()-1))
					{
						returnList.add(code);
					}
			count++;
		}
		return returnList;
		
	}
	private ArrayList<HTTPCode> getLastMinuteMessages(Date d) {
		ArrayList<HTTPCode> returnList = new ArrayList<HTTPCode>();
		int count = 0;
		for(HTTPCode code : codesList)
		{
			if (count <=100 &&code.getTime().getMonth() == d.getMonth() && code.getTime().getDay() == d.getDay() && code.getTime().getHours() == d.getHours() &&
					(code.getTime().getMinutes() <= d.getMinutes() && code.getTime().getMinutes() >= d.getMinutes()-1))
					{
						returnList.add(code);
					}
			count++;
		}
		return returnList;
	}
	private ArrayList<HTTPCode> getCodes(String type)
	{
		ArrayList<HTTPCode> returnList = new ArrayList<HTTPCode>();
		for(HTTPCode code : codesList)
		{
			int temp = Integer.parseInt(code.getCode());
			if (temp == Integer.parseInt(type))
			{
				returnList.add(code);
			}
		}
		return returnList;
	}
	private String createJSON(ArrayList<HTTPCode> list)
	{
		String beg = "{\"Processing Chunks\" :{";
		String middle = "";
		String end = "}}";
		for (HTTPCode code : list) 
		{
			middle = middle + code.toString();
		}
		return beg + middle + end;
		
	}
}
