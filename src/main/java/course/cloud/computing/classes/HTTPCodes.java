package course.cloud.computing.classes;

import java.util.ArrayList;
import java.util.Collections;

public class HTTPCodes 
{
	private ArrayList<HTTPCode> codesList = new ArrayList<HTTPCode>();
	
	public void addCode(HTTPCode code)
	{
		codesList.add(code);
		Collections.sort(codesList);
	}
	public String getErrors(String type)
	{
		return createJSON(getCodes(type));	
	}
	public String getSuccess(String resolution)
	{
		switch (resolution) {
		case "minutes":
			//codesList.
			break;
		case "hours":
					
					break;
		case "days":
			
			break;
		case "months":
			
			break;
		default:
			break;
		}
		return null;
		
	}
	private ArrayList<HTTPCode> getCodes(String type)
	{
		ArrayList<HTTPCode> returnList = new ArrayList<HTTPCode>();
		for(HTTPCode code : codesList)
		{
			if (code.getCode() != type)
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
