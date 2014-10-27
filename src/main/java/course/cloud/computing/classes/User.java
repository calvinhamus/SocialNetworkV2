package course.cloud.computing.classes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement 
public class User 
{
	int id;
	String name;
	String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	@Override
//	public String toString() {
//		return "{\"User\":{ \"id\":" + id + ", \"name\":" + "\""+name +"\""+ ", \"message\":" + message +"}}";
//	}
}
