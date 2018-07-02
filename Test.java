package DataFactory.artifact;

import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Test {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		Scanner in=new Scanner(System.in);
		
		System.out.println("Enter the number of tables");
		int n=in.nextInt();
		in.nextLine();
		
		TableCreater R1[]=new TableCreater[n];
		//Thread t1[]=new Thread[n];
		String path[]=new String[n];
		
		System.out.println("Enter the complete path of all the tables");
		for(int i=0;i<n;i++)
		{
			path[i]=in.nextLine();
		}
		
		for(int i=0;i<n;i++)
		{
			R1[i]=new TableCreater();
			R1[i].path(path[i]);
			//t1[i]=new Thread(R1[i]);
			R1[i].setName("Thread "+ (i+1));
			R1[i].start();
		}
		
		
		}
	}



/*
 * 
 * 
 *Scanner in=new Scanner(System.in);
		
		System.out.println("Enter the number of tables");
		int n=in.nextInt();
		in.nextLine();
		
		TableCreater R1[]=new TableCreater[n];
		Thread t1[]=new Thread[n];
		String path[]=new String[n];
		
		System.out.println("Enter the complete path of all the tables");
		for(int i=0;i<n;i++)
		{
			path[i]=in.nextLine();
		}
		
		for(int i=0;i<n;i++)
		{
			R1[i]=new TableCreater();
			R1[i].path(path[i]);
			t1[i]=new Thread(R1[i]);
			t1[i].start();
		}
		

*/