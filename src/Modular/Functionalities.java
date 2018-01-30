package Modular;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.sql.*;
import java.io.*;
import org.jfree.ui.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.*;
import org.jfree.data.jdbc.JDBCCategoryDataset;
public class Functionalities implements MonitorService,Runnable {
	    
	    private Connection connect=null;
	    private Statement statement=null;
	    private ResultSet resultSet=null;
	    HashMap<String,MyTuple> hm=new HashMap<String,MyTuple>();  
	    private PreparedStatement preparedStatement=null;
		private double dp1=5242880;
		private double dp=5242880;
		private double min_dp=dp1;
		private double max=0;
		private double avgsum=0;
		private double finalsum=0;
		private double denominator=365;
		private double min_diff=100;
		private double min_per_day=100;
		private double max_diff=0;
		private double avg_diff=0;
		private double first=0;
		private double last=0;
		private double temp=0;
		private int flag=0;
		private double min_days;
		private double max_days;
		private double avg_days;
		private static Functionalities obj=null;
		public static Functionalities getinstance()
		{
			if(obj==null)
			{
				obj=new Functionalities();
			}
			return obj;
		}
		public void run()
		{
			while(true)
			{
				synchronized(obj)
				{
					try
					{
						obj.wait(5000);
						Store(MySqlCollectData());
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
				}
			}
		}
      	public HashMap<String,MyTuple> MySqlCollectData()throws Exception
      	{   
      		
      		try
      		{   
      			Class.forName("com.mysql.jdbc.Driver");
      			connect=DriverManager.getConnection("jdbc:mysql://localhost/mysql?"+"user=root&password=root");
      			statement=connect.createStatement();
      			String[] s = {"Uptime","Bytes_sent","Bytes_received","Slow_queries","Questions"};
      			
    			for(int i=0;i<s.length;i++)
    			{
    			  resultSet=statement.executeQuery("show global status like '"+s[i]+"'");
    			  resultSet.absolute(1); 
    			  Double value=resultSet.getDouble(2);
    			  long ms=System.currentTimeMillis();
    	          MyTuple element=new MyTuple(ms,value);
    	          hm.put(s[i],element);
    	          System.out.println(hm);
    			}
    			return hm;
      		}
      		catch(Exception e)
      		{
      			System.out.println(e);
      		}
      		
      		finally
    		{
    			close();
    		}
			return hm;
      	}
      	public void Store(HashMap<String,MyTuple> hm)throws Exception
      	{
      		try
      		{
      			Class.forName("com.mysql.jdbc.Driver");
      			connect=DriverManager.getConnection("jdbc:mysql://localhost/intern?"+"user=root&password=root");
      			preparedStatement=connect.prepareStatement("insert into intern.Store values(default,?,?,?,?)");
      			for ( String key : hm.keySet())
      			{
      				preparedStatement.setString(1,key);
      				MyTuple res=hm.get(key);
      				preparedStatement.setDouble(2,res.value);
      				preparedStatement.setLong(3,res.time);
      				preparedStatement.setString(4,"MySQL");
      				preparedStatement.executeUpdate();
      			}
      		}
      		catch(Exception e)
      		{
      			System.out.println(e);
      			throw e;
      		}
      		finally
    		{
    			close();
    		}
      	}
      	public JPanel DrawGraphs()
      	{
      		try
      		{   
      			JPanel panel=new JPanel();
      			String[] str = {"Uptime","Bytes_sent","Bytes_received","Slow_queries","Questions"}; 
      			for(int i=0;i<str.length;i++)
      			{
      				String query= "SELECT time,datapoint from Store where mname ='"+str[i]+"'";
      				JDBCCategoryDataset dataset = new JDBCCategoryDataset("jdbc:mysql://localhost:3306/intern", "com.mysql.jdbc.Driver","root", "root");
      				dataset.executeQuery(query);
      				JFreeChart chart = ChartFactory.createLineChart(str[i], "time", "datapoint",dataset, PlotOrientation.VERTICAL, true, true, false);
      				ChartPanel chartPanel = new ChartPanel(chart);
      				chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
      				panel.add(chartPanel);
      			}
      			return panel;
      			
      		}
      		catch(Exception e)
      		{
      			System.out.println(e);
      		}
      		finally
      		{
      			close();
      		}
      		
      		return null;
      	}
      	public JScrollPane Analyse()
      	{
      		try
    		{   
    			Class.forName("com.mysql.jdbc.Driver");
    			connect=DriverManager.getConnection("jdbc:mysql://localhost/intern?"+"user=root&password=root");
    			statement=connect.createStatement();
    			for(long i=1;i<105120;i+=288)
                {   
                	resultSet=statement.executeQuery("SELECT * FROM bigdata LIMIT "+i+",288");
                	avgsum=0;
                	if(flag==0)
                	{
                		resultSet.next();
                		flag=1;
                	}
                    while(resultSet.next())
                	{
                		first=resultSet.getDouble("datapoint");
                		temp=first-dp1;
                		dp1=first;
                		if(temp<min_diff)
                			min_diff=temp;
                		if(temp>max_diff)
                		{   
                			max_diff=temp;
                		}
                	    avgsum=avgsum+temp;
                	
                	    flag=0;
                		
                	}
                    avgsum=avgsum/288;
                    finalsum=finalsum+avgsum;	
                }
    			finalsum=finalsum/365;
    			finalsum=finalsum*288;
    			min_diff=min_diff*288;
    			max_diff=max_diff*288;
    			min_days=5190340/min_diff;
    			max_days=5190340/max_diff;
    			avg_days=5190340/finalsum;
    			
    			JFrame frame = new JFrame("Bigdata Anylatics");
    		    frame.setSize(300,300);
    		    String columns[] = {"MIN_DAYS", "MAX_DAYS", "AVERAGE_DAYS"};
    		    Double data[][] ={{min_days,max_days,avg_days}};
    		    JTable t=new JTable(data, columns);
    		    t.setBounds(30,40,200,300);
    		    JScrollPane scrollPane = new JScrollPane(t);
    		    return scrollPane;
    		}
    		catch(Exception e)
    		{
    			System.out.println(e);
    		}
    		finally
    		{
    			close();
    		}
      		return null;
      	}
      	public void drawAnalytics()
      	{
      		System.out.println("haiiiiii");
      	}
      	
    	private void close()
    	{
    		try
    		{
    			if(resultSet !=null)
    			{
    				resultSet.close();
    			}
    			if(statement !=null)
    			{
    				statement.close();
    			}
    			if(connect !=null)
    			{
    				connect.close();
    			}
    		}
    		catch (Exception e)
    		{
    			
    		}
    		
    	}
		
}
