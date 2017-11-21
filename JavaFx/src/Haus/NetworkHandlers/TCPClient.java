package Haus.NetworkHandlers;

import Haus.Application.Controllers.TeacherController;
import Haus.Application.Parser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * This class handles the connection between the application and the server.
 *
 * @author Anthony Path and Laiz Figueroa
 * @version 1.0
 *
 */

public class TCPClient {
public static  String fromServ="nope";
public static String teacherUsername="";
public static String studentUsername="";
    @SuppressWarnings("rawtypes")
	public static String main(String user, String ip) throws Exception {

        String sentence; // string to hold messages
        String modifiedSentence; // string to receive messages
       
        String request = "request";
      

        

        if (user == "teacher") {
        	 String message = Parser.Parse2(TeacherController.toParse,true).toString()+"~"+Parser.ParseInorder(TeacherController.toParse).toString();
        	 Reader inputData = new StringReader(message);
             BufferedReader inFromUser = new BufferedReader(inputData);

             Reader inputRequest = new StringReader(request);
             BufferedReader inRequest = new BufferedReader(inputRequest);
            ip  = Inet4Address.getLocalHost().getHostAddress();
            Socket clientSocket = new Socket(ip, 8080);
            System.out.println(clientSocket);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            // here we receive msg from server
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Object [] users = Parser.Parse2(TeacherController.toParse,false).keySet().toArray();
            String gateway = "";
            List <Object> ordered = new ArrayList <Object>();
            for (int i=0;i<users.length;i++){
            	if (!users[i].toString().contains("Gateway")){
            	  ordered.add(users[i]+"+"); //+ servers as indicator for Erlang to add "\n" in its place so that Java can read username later
            	}else{
            	gateway=users[i].toString();}
            }
           ordered.add(gateway+"+");
            
            
          
          
          
            outToServer.writeUTF(message+"!^!"+ordered.toString().replace('[', ' ').replace(']', ' ')+"!?!"+"TEACHER\n"); // send it to server
            request = inRequest.readLine(); // Ask to send the file
            System.out.println("Request file" + request);
            fromServ = inFromServer.readLine(); // Receive the parsed file
            System.out.println("Teacher username: " + fromServ.substring((fromServ.indexOf("g")-1),fromServ.length())); 
            teacherUsername=fromServ;


            clientSocket.close();
            System.out.println("Socket closed!");

        } else {
            Socket clientSocket = new Socket(ip, 8080);
            System.out.println(clientSocket);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            // here we receive msg from server
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

          //  sentence = inRequest.readLine(); // Get the request from the user to the server
            outToServer.writeUTF("GET" + '\n'); // Send the request
            fromServ = inFromServer.readLine(); // Receive the parsed file
            
            System.out.println("FROM SERVER: " + fromServ);
            String [] temp = fromServ.split("!*!");
            studentUsername=temp[1];
            fromServ=temp[0];

            clientSocket.close();
            System.out.println("Socket closed!");

        }
		return fromServ;
    }
}
