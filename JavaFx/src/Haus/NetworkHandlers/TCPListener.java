package Haus.NetworkHandlers;
import java.io.*;
import java.net.*;

import Haus.TechnicalFramework.Controllers.AnimationController;
public class TCPListener extends Thread {
	

	
	 /*public static void main(String args[]) throws Exception {
		 listen();
	 }*/
	 public static void listen() throws IOException {
		 
	  String serverSentence;
	  String capitalizedSentence;
	  ServerSocket welcomeSocket = new ServerSocket(6789);
	 

	  while (true) {
	  
		  Socket connectionSocket = welcomeSocket.accept();
	   BufferedReader inFromClient =
	    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	   DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	   serverSentence = inFromClient.readLine();
	   System.out.println("Received: " + (serverSentence));
	   String toSend=serverSentence.substring(0,serverSentence.indexOf(','));
	   System.out.println("Sending: " + (toSend));
	  // outToClient.writeUTF(toSend+"!^!CONFIRM!?!STUDENT" + '\n');
	   connectionSocket.close();
	  // System.out.println("Sending confirmation ID: "+toSend);
      try{Integer.parseInt(toSend);
	   TCPClient.sendMessage(toSend,true);}
	  catch (Exception e){
		  //do nothing
	  }
	  }
	 
	}
}
