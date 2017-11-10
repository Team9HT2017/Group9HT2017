package Haus;

import java.io.*;
import java.net.*;
/**
 * This class handles the connection between the application and the server.
 *
 * @author Anthony Path and Laiz Figueroa
 * @version 1.0
 *
 */

public class TCPClient {
public static  String fromServ="nope";
    public static String main(String user, String ip) throws Exception {

        String sentence; // string to hold messages
        String modifiedSentence; // string to receive messages
        String message = Parser_v1.Parse2(TeacherController.toParse).toString();
        String request = "request";
      

        Reader inputData = new StringReader(message);
        BufferedReader inFromUser = new BufferedReader(inputData);

        Reader inputRequest = new StringReader(request);
        BufferedReader inRequest = new BufferedReader(inputRequest);

        if (user == "teacher") {
            ip  = Inet4Address.getLocalHost().getHostAddress();
            Socket clientSocket = new Socket(ip, 8080);
            System.out.println(clientSocket);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            // here we receive msg from server
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        //    sentence = inFromUser.readLine(); // here we get the parsed doc
            outToServer.writeUTF(message + '\n'); // send it to server
            request = inRequest.readLine(); // Ask to send the file
            System.out.println("Request file" + request);
            fromServ = inFromServer.readLine(); // Receive the parsed file
            System.out.println("FROM SERVER: " + fromServ);



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


            clientSocket.close();
            System.out.println("Socket closed!");

        }
		return fromServ;
    }
}
