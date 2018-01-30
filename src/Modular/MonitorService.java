package Modular;


import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public interface MonitorService 
{
    
	public HashMap<String,MyTuple> MySqlCollectData()throws Exception;
	public void Store(HashMap<String,MyTuple> hm)throws Exception;
	public JPanel DrawGraphs();
	public JScrollPane Analyse();
	public void drawAnalytics();
	
	
}
