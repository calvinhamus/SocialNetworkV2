package course.cloud.computing.classes;

import java.util.Date;

public class HTTPCode  implements Comparable<HTTPCode>                                                                    
{
	private String code = "";
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	private Date time = new Date();
	public HTTPCode(String cd)
	{
		this.code = cd;
	}
//	@Override
//	public String toString() {
//		return "{\"HTTPCode\":{ \"Code\":" + code + ", \"Time\":" + time +"}}";
//	}
	@Override
	public int compareTo(HTTPCode compairCode) {
		return getTime().compareTo(compairCode.getTime());
	}
}
