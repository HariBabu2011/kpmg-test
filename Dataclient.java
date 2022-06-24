package ksm.dataserver;

import java.io.*;
import java.net.*;


public class DataClient
{
	public DataClient()
	{
		Processing();

	}
	public void Processing()
	{
		try
		{
			Socket client=new Socket("localhost",4445);
			BufferedReader br;
			BufferedWriter bw;
			String strmsg;
			br=new BufferedReader(new InputStreamReader(client.getInputStream()));
			bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			System.out.println("Connected to "+client.getInetAddress().getHostName());



			String strinput=null;
			String strrecv=null;
			do
			{


				//input from keyborad			
				System.out.println("Enter Query Data:\t");
				BufferedReader stdIn = new BufferedReader(
	                                   new InputStreamReader(System.in));
	            strinput=stdIn.readLine();
	                          
            	//send query data
                bw.write(strinput+"\n");
				bw.flush();

				//receive result
				strrecv=br.readLine();
				if (strrecv!=null)
					System.out.println("SERVER>>\t"+strrecv);

			}
			while(strinput.compareTo("bye")!=0);
			bw.close();
			br.close();
			//client.close();

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public static void main(String a[])
	{
		new DataClient();

	}
}


