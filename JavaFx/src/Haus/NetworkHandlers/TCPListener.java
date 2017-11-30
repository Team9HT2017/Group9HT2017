package Haus.NetworkHandlers;
import java.io.*;
import java.net.*;
import java.util.Arrays;

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
	   System.out.println("Sender/receiver: " + serverSentence.substring(serverSentence.indexOf("{")+2, serverSentence.indexOf("send")-3));
	  // outToClient.writeUTF(toSend+"!^!CONFIRM!?!STUDENT" + '\n');
	   connectionSocket.close();
	   System.out.println(Arrays.toString(getSenderRecipient(serverSentence)));
	  // System.out.println("Sending confirmation ID: "+toSend);
      try{Integer.parseInt(toSend);
	   TCPClient.sendMessage(toSend,true);}
	  catch (Exception e){
		  //do nothing
	  }
	  }
	 
	}
	 
	 public static String [] getSenderRecipient (String message){
	    	String sender = message.substring(message.indexOf("{")+2, message.indexOf("send")-3);
	    	String recipient = message.substring(message.indexOf("to")+3,message.indexOf("the")-3);
	    	System.out.println("Sender:"+sender +"  Recipient:"+recipient);
	    	String [] res = {sender,recipient};
			return res;  	
	    }
}
