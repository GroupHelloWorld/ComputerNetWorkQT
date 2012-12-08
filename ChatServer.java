

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


class ClientThread  implements Runnable
{
  final int MAXN = 10;
	Socket[] socket = new Socket[MAXN];
	int clientNum;
	boolean needupdate = false;
	public ClientThread(Socket[] s1, int num){
		for(int i=0;i<=num;i++)
			socket[i] = s1[i];
		clientNum = num;

	}
	public void run(){
		try{
		while(true){
		   	DataInputStream inputFromClient = new DataInputStream(socket[clientNum].getInputStream());
			 String s1= inputFromClient.readUTF();
			 SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
			 String time = dateformat.format(new Date());
			 s1 = time +"\n" + s1;
			System.out.println(s1+"\n");
			for(int i=0;i<=clientNum;i++){
				Runnable write = new Write(socket[i],s1);
				Thread thread = new Thread(write);
				thread.start();
			}
		}
		}
		 catch(IOException ex){
		System.err.println(ex);
     }
	}
}

class Write implements Runnable
{
	Socket socket;
	String info;
	public Write(Socket s, String s1){
		socket = s;
		info = s1;
	}
	public void run(){
		try{
			DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
			outputToClient.writeUTF(info+"\n");   
		}
		 catch(IOException ex){
			System.err.println(ex);
		}
	}
}
public class ChatServer
{
	public static void main(String[] args) 
	{
		final int MAXN = 10;
		int clientNum = 0;
	    Socket[] socket = new Socket[MAXN];
		ClientThread[] clientThread = new ClientThread[MAXN];
		try{
			ServerSocket serverSocket = new ServerSocket(8000);
			while(true){
				socket[clientNum] = serverSocket.accept();
				DataInputStream inputFromClient = new DataInputStream(socket[clientNum].getInputStream());
			    String name = inputFromClient.readUTF();   //获得新加入用户名字
				System.out.println(name+" enter!\n");
				for(int i=0;i<clientNum;i++){   //发送给其他客户端
					DataOutputStream outputToClient = new DataOutputStream(socket[i].getOutputStream());
					outputToClient.writeUTF(name+" enter!\n");  							
				}
				clientThread[clientNum] = new ClientThread(socket,  clientNum);
				Thread thread = new Thread(clientThread[clientNum]);
				thread.start();
				clientNum++;
			}
		}
		catch(IOException ex){
			System.err.println(ex);
		}
	}
	
}
