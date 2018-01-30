package Modular;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
public class Analytics
{
	private Connection connect=null;
	private PreparedStatement preparedStatement=null;
	private Statement statement=null;
	private ResultSet resultSet=null;
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
	public void Populate()
	{  
		try
		{   
			Class.forName("com.mysql.jdbc.Driver");
			connect=DriverManager.getConnection("jdbc:mysql://localhost/intern?"+"user=root&password=root");
			preparedStatement=connect.prepareStatement("insert into intern.bigdata values(default,?,?)");
			
			long ms=System.currentTimeMillis();
			System.out.println("Start time="+System.currentTimeMillis());
			for(long i=1;i<105121;i++)
			{   
			
				preparedStatement.setDouble(1,dp);
				double r=Math.random();
				dp=dp+r*10000;
				preparedStatement.setLong(2,ms);
				ms=ms+300000;
				preparedStatement.executeUpdate();
				
			}
			System.out.println("End time="+System.currentTimeMillis());
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			close();
		}
		
	}
	public void equation()
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
            		//System.out.println("first:"+first);
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
			System.out.println("min_days"+min_diff);
			System.out.println("max_days"+max_diff);
			System.out.println("avg_days"+finalsum);
			JFrame frame = new JFrame("Bigdata Anylatics");
		    frame.setSize(300,300);
		    String columns[] = {"MIN_DAYS", "MAX_DAYS", "AVERAGE_DAYS"};
		    Double data[][] ={{min_days,max_days,avg_days}};
		    JTable t=new JTable(data, columns);
		    t.setBounds(30,40,200,300);
		    JScrollPane scrollPane = new JScrollPane(t);
		    frame.add(scrollPane);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(550, 200);
	        frame.setVisible(true);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			close();
		}
		
	}
	private void close()
	{
		try
		{
			if(preparedStatement!=null)
			{
				preparedStatement.close();
			}
			if(connect !=null)
			{
				connect.close();
			}
		}
		catch (Exception e)
		{
			
		}
		{
		 
		}
	}
}

