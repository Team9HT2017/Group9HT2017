package Haus.NetworkHandlers;

import Haus.TechnicalFramework.Controllers.TeacherController;
import Haus.TechnicalFramework.DataHandler.Parser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
/**
 * This class handles the connection between the application and the server.
 *
 * @author Anthony Path and Laiz Figueroa
 * @version 1.0
 *
 */

public class TCPClient {
public static  String fromServ = "nope";
public static String globalIP;
public static String teacherUsername="";
public static String studentUsername="";

    @SuppressWarnings("rawtypes")
	public static String main(String user, String ip, String message) throws Exception {

        String sentence; // string to hold messages
        String modifiedSentence; // string to receive messages
       
        String request = "request";
      
        globalIP=ip;

        if (user == "teacher") {
        	 //String message = Parser.Parse2(TeacherController.toParse,true).toString()+"~"+Parser.ParseInorder(TeacherController.toParse).toString();
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

            	  ordered.add(users[i].toString().split("\\|")[0].replaceAll("\\s+", "")); //+ servers as indicator for Erlang to add "\n" in its place so that Java can read username later

            	}else{
            	gateway=users[i].toString().split("\\|")[0].replaceAll("\\s+", "");}
            }
            if (!gateway.equals("")){
           ordered.add(gateway);}

          
            outToServer.writeUTF(message+"!^!"+ordered.toString().replace('[', ' ').replace(']', ' ').replaceAll("\\s+", "")+"!?!"+"TEACHER\n"); // send it to server
            request = inRequest.readLine(); // Ask to send the file
            System.out.println("Request file" + request);
            fromServ = inFromServer.readLine(); // Receive the parsed file
           
            
            String [] artest = fromServ.substring(0, fromServ.length()-4).split(",");
            List teacherN = new LinkedList <String>(Arrays.asList(artest));
            System.out.println("Teacher usernames: " + teacherN.toString());
            teacherN.remove(0);
            System.out.println("Teacher usernames: " + teacherN.toString());
            teacherUsername=Arrays.toString(teacherN.toArray()).replaceAll("\\[", "").replaceAll("\\]", "");//fromServ.substring((fromServ.indexOf(":")+2),fromServ.length());
            System.out.println("Teacher username: " + teacherUsername);
            fromServ=teacherN.toString();
            clientSocket.close();
            System.out.println("Socket closed!");
        } else {
            Socket clientSocket = new Socket(ip, 8080);
            System.out.println(clientSocket);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            // here we receive msg from serverl
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

          //  sentence = inRequest.readLine(); // Get the request from the user to the server
            outToServer.writeUTF("GET" + '\n'); // Send the request
            fromServ = inFromServer.readLine(); // Receive the parsed file
            
            System.out.println("FROM SERVER: " + fromServ);
            String [] temp = fromServ.split("!*!");
            System.out.println(Arrays.toString(temp));
            studentUsername=temp[2];
            System.out.println("Students username: " + studentUsername);
            fromServ=temp[0];
            clientSocket.close();
            System.out.println("Socket closed!");

        }
		return fromServ;
    }
    
    
    public static void sendMessage(String message,boolean confirm) throws UnknownHostException, IOException{
    	 Socket clientSocket = new Socket(globalIP, 8080);
         System.out.println(clientSocket);

         DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
         if (!confirm){
         outToServer.writeUTF(message+"!^!SEND!?!STUDENT" + '\n'); // Send the request
         }else {
        	 outToServer.writeUTF(message+"!^!CONFIRM!?!STUDENT" + '\n');
         }
         clientSocket.close();

    }
    public static void searchMeassage(ArrayList<? extends Object>  message ) throws  IOException{
        Socket clientSocket = new Socket(globalIP, 8080);
        System.out.println(clientSocket);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        String sendToServer =message.toString();
        outToServer.writeUTF(sendToServer+ "!?!SEARCH" + '\n' );
        clientSocket.close();

    }
   
}
