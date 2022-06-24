 package ksm.dataserver;


import java.io.*;
import java.net.*;



public class MiddleServer
{
	public ServerSocket Server;
	public Socket 		Client;
	public Socket 		LocalClient; //same server

	public MiddleServer()
	{
		Init();


	}
	public void Init()
	{
		try
		{
			Server=new ServerSocket(4445);
			LocalClient=new Socket("localhost",4444);
			System.out.println("Connected to DataBase Server:\t");
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

	public void Processing()
	{
		try
		{
			BufferedReader br_C;
			BufferedWriter bw_C;
			BufferedReader br_LC;
			BufferedWriter bw_LC;
			String strmsg;

			br_C=new BufferedReader(new InputStreamReader(Client.getInputStream()));
			bw_C=new BufferedWriter(new OutputStreamWriter(Client.getOutputStream()));


			//localclient
			br_LC=new BufferedReader(new InputStreamReader(LocalClient.getInputStream()));
			bw_LC=new BufferedWriter(new OutputStreamWriter(LocalClient.getOutputStream()));

			String strquery="";
			String strresult="";
			String strquerytext="";
			String strtemp="";
		int countrecords=0;

		do
		{

					//read from the client
					strquery=br_C.readLine();
					System.out.println("CLIENT>>:\t"+strquery);
					if (strquery.compareTo("bye")==0)
					{
						bw_LC.write(strquery+"\n");
						bw_LC.flush();


					}

					else if (strquery.compareTo("bye")!=0 && strquery!=null )
					{
						//send to dataserver
						strquerytext="SELECT * FROM stdinfo where id='"+strquery+"'";
						//strquerytext="SELECT * FROM stdinfo WHERE id='1'";
						bw_LC.write(strquerytext+"\n");
						bw_LC.flush();

						do
						{

							//rec result from dataserver
							strtemp=br_LC.readLine();
							//System.out.println(strtemp);
							if (strtemp!=null)
							{
								if(strtemp.compareTo("end")!=0)
								{
									strresult+=strtemp;
									countrecords++;
								}

							}

						}
						while(strtemp.compareTo("end")!=0);
						System.out.println("DB SERVER>>\t"+strresult);



						//send result to Client
						bw_C.write(strresult+"\tTotal Records= "+countrecords+"\n");
						bw_C.flush();
						strresult="";
						countrecords=0;
				}
			}
		while(strquery.compareTo("bye")!=0);
		br_C.close();
		bw_C.close();
		System.out.println("Client:\t"+Client.getInetAddress().getHostName()+" terminated");
		Client.close();

		br_LC.close();
		bw_LC.close();




		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}




	public static void main(String a[])
	{
		System.out.println("Starting MiddleServer...");
		new MiddleServer();
	}
} 