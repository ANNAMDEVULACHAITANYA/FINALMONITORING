package Modular;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
public class Main 
{   
	static JPanel panel=null;
	static JScrollPane scrollpane=null;
	public static void main(String[] args)
	{          
		        JFrame f=new JFrame("Monitoring Metrics");
		        JPanel monitor=new JPanel();
		        JButton start=new JButton("START");
		        start.setBounds(50,100,95,30); 
		        JButton stop=new JButton("STOP");
		        stop.setBounds(50,100,95,30);
		        JButton getgraph=new JButton("GET GRAPH");
		        stop.setBounds(50,100,95,30);
		        monitor.add(start);
		        monitor.add(stop);
		        monitor.add(getgraph);
		        
		        JTabbedPane tp=new JTabbedPane(); 
		        tp.add("Monitor",monitor);
		        
		        Functionalities obj=Functionalities.getinstance();
		        Thread t=new Thread(obj);
		        start.addActionListener(new  ActionListener() 
		        {
					
					@Override
					public void actionPerformed(ActionEvent e)
					{
						t.start();
						System.out.println("Thread Started");
						
					}
				}); 
		        stop.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						t.stop();
						System.out.println("Thread Stopped");
					}
				});
		        getgraph.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						tp.removeAll();
						panel=obj.DrawGraphs();
						scrollpane=obj.Analyse();
						tp.add("MONITOR",monitor);
						tp.add("GRAPHS",panel);
						tp.add("Analytics", scrollpane);
						f.add(tp);
						f.pack();
						tp.setSelectedIndex(1);
		      			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		      			f.setVisible(true);
		      			
						
					}
				});
			    tp.add("Graphs", panel);
			    tp.add("Analytics", scrollpane);
		        f.add(tp);
				f.getContentPane().setBackground(Color.BLUE);
		        f.pack();
      			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      			f.setVisible(true);
	}
}
