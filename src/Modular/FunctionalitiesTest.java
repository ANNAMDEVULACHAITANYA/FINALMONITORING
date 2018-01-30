package Modular;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.junit.Test;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import static org.junit.Assert.*;

public class FunctionalitiesTest {
    
	HashMap<String,MyTuple> hm;
	private Connection connect=null;
	private Statement statement=null;
	private ResultSet resultSet=null;
	private int prev;
	private int after;
	@Test
	public void testMySqlCollectData()throws Exception 
	{   
		Functionalities fs=new Functionalities();
		System.out.println("Im in collecting test");
		try
		{
			hm=fs.MySqlCollectData();
			assertNotNull(hm);
		}
		catch(SQLException se)
		{
			assertTrue("SQL Exception",se instanceof SQLException);
		}
		catch(IndexOutOfBoundsException ie)
		{
			assertTrue(ie instanceof IndexOutOfBoundsException);
		}
	}
	@Test
	public void testStore() throws Exception {
		
		Functionalities fs=new Functionalities();
		try
		{  
			Class.forName("com.mysql.jdbc.Driver");
			connect=DriverManager.getConnection("jdbc:mysql://localhost/intern?"+"user=root&password=root");
			statement=connect.createStatement();
		    resultSet=statement.executeQuery("select count(*) from store");
		    System.out.println("hereeee");
		    resultSet.absolute(1);
		    prev = resultSet.getInt(1);
		    hm=fs.MySqlCollectData();
		    fs.Store(hm);
		    resultSet=statement.executeQuery("select count(*) from store");
		    resultSet.absolute(1);
	        after = resultSet.getInt(1);
		    fs.Store(null);
		    assertEquals("hashmap size is not as expected",4,hm.size());
			assertTrue("data not inserted",after>prev);
			System.out.println("size:"+hm.size());
		}
		catch(NullPointerException ne)
		{
			assertTrue("NullPointer exception araised",ne instanceof NullPointerException);
		}
		catch(SQLException e)
		{   
			System.out.println(e);
			assertTrue("SQL Exception araised",e instanceof SQLException);
		}
		catch(IndexOutOfBoundsException ae)
		{
			assertTrue("Array index out of bounds Exception araised",ae instanceof IndexOutOfBoundsException);
		}
		
	}
	/*@Test
	public void testDrawGraphs() {
		fail("Not yet implemented");
	}

	@Test
	public void testAnalyse() {
		fail("Not yet implemented");
	}

	@Test
	public void testDrawAnalytics() {
		fail("Not yet implemented");
	}*/

}
