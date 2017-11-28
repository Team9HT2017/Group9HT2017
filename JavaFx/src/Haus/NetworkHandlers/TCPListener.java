package Haus.NetworkHandlers;
import java.io.*;
import java.net.*;

import Haus.TechnicalFramework.Controllers.AnimationController;
public class TCPListener extends Thread {
	

	
	 /*public static void main(String args[]) throws Exception {
		 listen();
	 }*/
	 public static void listen() throws IOException {
		 
	  String clientSentence;
	  String capitalizedSentence;
	  ServerSocket welcomeSocket = new ServerSocket(6789);
	 

	  while (true) {
	  
		  Socket connectionSocket = welcomeSocket.accept();
	   BufferedReader inFromClient =
	    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	   DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	   clientSentence = inFromClient.readLine();
	   System.out.println("Received: " + clientSentence);
	   connectionSocket.close();
	   AnimationController.testMsg(clientSentence);
	   //outToClient.writeBytes(capitalizedSentence);
	  }
	 
	}
}
