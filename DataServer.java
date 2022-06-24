package ksm.dataserver;

import java.io.*;
import java.sql.*;
import java.net.*;

class DataServer 
 {
 	public ServerSocket	Server;
 	public Socket		Client;

	public DataServer() 
	{
		Init();

	}

	public void Init()
	{
		try
		{
			Server=new ServerSocket(4444);
			WaitingForConnection();
			Processing();

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public void WaitingForConnection()
	{
		System.out.println("Waiting For Connection "+Server.getInetAddress().getHostName());
		System.out.println("Listening on port: "+Server.getLocalPort());

		try
		{
			do
			{
				Client=Server.accept();

			}
			while(Client==null);
			System.out.println("Client Found:\t"+Client.getInetAddress().getHostName());



		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

/*	public void SendResult() 
	{
		try
		{
			ServerSocket server=new ServerSocket(4444);
			Socket client;
			BufferedReader br;
			BufferedWriter bw;
			String strmsg;
			String strresult=new String();
			String strquery="select * from stdinfo where id= ";
			int itotalcolumns;

			//loads the driver 
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//make connection
			Connection conn =DriverManager.getConnection("jdbc:odbc:std");
			//create statement 
			Statement statement=conn.createStatement();
			System.out.println("Listening on port: "+server.getLocalPort());

			while(true)
			{
				client=server.accept();
				br=new BufferedReader(new InputStreamReader(client.getInputStream()));
				bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				System.out.println("Connected to "+client.getInetAddress().getHostName());
				strquery+=br.readLine();
				//Query 
				ResultSet rs=statement.executeQuery(strquery);
				//Getting MetaData
				ResultSetMetaData metadata=rs.getMetaData();
				itotalcolumns=metadata.getColumnCount();

				//Extracting from Resultset

				while(rs.next())
				{
					for(int i=1;i<=itotalcolumns;i++)
					{
						strresult+=rs.getString(i)+"\t";

					}
					strresult="";
					bw.write(strresult);
					bw.flush();
				}
				client.close();
				br.close();
				bw.close();
				System.out.println("connection closed");



			}

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}*/

	public void Processing()
	{
		try
		{
			BufferedReader br;
			BufferedWriter bw;

			//String strmsg;

			br=new BufferedReader(new InputStreamReader(Client.getInputStream()));
			bw=new BufferedWriter(new OutputStreamWriter(Client.getOutputStream()));

			String strquerytext=new String();
			String strresult=new String();



			//database hadling
			//loads the driver 
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//make connection
			Connection conn =DriverManager.getConnection("jdbc:odbc:std");
			//create statement 
			Statement statement=conn.createStatement();
			do
			{


				//read from the client
				strquerytext=br.readLine();
				System.out.println("CLIENT>>\t"+strquerytext);
				if (strquerytext.compareTo("bye")==0)
					break;

				//Query 
				ResultSet rs=statement.executeQuery(strquerytext);
				//Getting MetaData
				ResultSetMetaData metadata=rs.getMetaData();
				int itotalcolumns=metadata.getColumnCount();

				//Extracting from Resultset

				while(rs.next())
				{
					for(int i=1;i<=itotalcolumns;i++)
					{
						strresult+=rs.getString(i)+"\t";

					}
					System.out.print(strresult+"\n");
					//send result to client
					bw.write(strresult+"\n");
					bw.flush();
					strresult="";
				}
					bw.write("end"+"\n");
					bw.flush();
					//strquerytext="bye";

				//processing input from client
				//strresult="server= "+strquerytext;




			}
			while(strquerytext.compareTo("bye")!=0);
			br.close();
			bw.close();
			System.out.println("Client:\t"+Client.getInetAddress().getHostName()+" terminated");
			Client.close();


		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}


	public static void main(String args[]) {
		System.out.println("Starting DataServer...");
		new DataServer();
	}
}