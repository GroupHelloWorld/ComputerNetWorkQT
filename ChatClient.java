import java.util.Scanner;
import java.net.*;
import java.io.*;

public class ChatClient {
  public static void main(String[] args) {
     try{
  	 Socket socket = new Socket("localhost",8000);  
		 System.out.println("input you name: ");
		 Scanner scanner = new Scanner(System.in); 
		String name = scanner.nextLine();      //用户名
		DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
		toServer.writeUTF(name);  //把用户名传给服务器
        Receive receive = new Receive(socket);
		Thread thread = new Thread(receive);
		thread.start();
        while(true){
		 scanner = new Scanner(System.in); 
		 String s1 = scanner.nextLine();
		 s1 = name+":  "+s1;    //把用户名和输入传给服务器
         toServer = new DataOutputStream(socket.getOutputStream());
         toServer.writeUTF(s1);		 
     }
	 }
     catch(IOException ex){
        System.err.println(ex);
     }
  }
}

class Receive implements Runnable
{
	Socket socket;
	public Receive(Socket s){
		socket = s;
	}
	public void run(){
		try{
		while(true){	
		 DataInputStream fromServer = new DataInputStream(socket.getInputStream());
		 String s2 = fromServer.readUTF();
         System.out.println(s2); 
		}
		}
		catch(IOException ex){
        System.err.println(ex);
		}
	}
}