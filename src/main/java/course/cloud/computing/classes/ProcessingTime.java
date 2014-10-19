package course.cloud.computing.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement 
public class ProcessingTime 
{
	private ArrayList<Long> processTimeList = new ArrayList<Long>();
	
	public ArrayList<Long> getProcessTimeList() {
		return processTimeList;
	}

	public void setProcessTimeList(ArrayList<Long> processTimeList) {
		this.processTimeList = processTimeList;
	}

	public void addTime(long time)
	{
		processTimeList.add(time);
		Collections.sort(processTimeList);
		
	}
	private ArrayList<ProcessChunk> getProcessChunks()
	{
		ArrayList<ProcessChunk> chunks = new ArrayList<ProcessChunk>();
		int length = (int) Math.ceil(processTimeList.size()/10);
		int percent = 10;
		for (int g = 0;g  < processTimeList.size(); g +=length) {
			int end = Math.min(g + length, processTimeList.size());
			chunks.add(calcAverage(processTimeList.subList(g, end),percent));
			percent += 10;
		}
		return chunks;
	}

	private ProcessChunk calcAverage(List<Long> list, int percent) 
	{
		long sum = 0;
		for (int i = 0; i < list.size(); i++) 
		{
			sum += list.get(i);
		}
		
		return new ProcessChunk(sum, percent);
		
	}
	@Override
	public String toString() {
		String beg = "{\"Processing Chunks\" :{";
		String middle = "";
		String end = "}}";
		for (ProcessChunk chunk : getProcessChunks()) 
		{
			middle = middle + chunk.toString();
		}
		return beg + middle + end;
	}

}
