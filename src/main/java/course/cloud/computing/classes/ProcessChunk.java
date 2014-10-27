package course.cloud.computing.classes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement 
public class ProcessChunk 
{
	private long average = 0;
	private String section ="";
	
	public ProcessChunk(long avg,int percent)
	{
		this.average = avg;
		this.section = "Top: " + percent + "% = " + avg;
	}
	
	public long getAverage() {
		return average;
	}
	public void setAverage(long average) {
		this.average = average;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
//	@Override
//	public String toString() {
//		return "{\"Chunk\":{  \"Percent\":" + section +"}}";
//	}
	
}
