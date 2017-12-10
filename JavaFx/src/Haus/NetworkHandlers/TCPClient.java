package Haus.NetworkHandlers;

import Haus.TechnicalFramework.Controllers.TeacherController;
import Haus.TechnicalFramework.DataHandler.Parser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class handles the connection between the application and the server.
 *
 * @author Anthony Path and Laiz Figueroa
 * @version 1.0
 *
 */

public class TCPClient {
    public static String fromServ = "nope";
    public static String globalIP;
    public static String teacherUsername = "";
    public static String studentUsername = "";

    @SuppressWarnings("rawtypes")
    public static String main(String user, String ip, String message) throws Exception {

        String sentence; // string to hold messages
        String modifiedSentence; // string to receive messages

        String request = "request";

        globalIP = ip;

        if (user == "teacher") {
            //String message = Parser.Parse2(TeacherController.toParse,true).toString()+"~"+Parser.ParseInorder(TeacherController.toParse).toString();
            Reader inputData = new StringReader(message);
            BufferedReader inFromUser = new BufferedReader(inputData);

            Reader inputRequest = new StringReader(request);
            BufferedReader inRequest = new BufferedReader(inputRequest);
            ip = Inet4Address.getLocalHost().getHostAddress();
            Socket clientSocket = new Socket(ip, 8080);
            System.out.println(clientSocket);


            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            // here we receive msg from server
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Object[] users = Parser.Parse2(TeacherController.toParse, false).keySet().toArray();
            String gateway = "";
            List<Object> ordered = new ArrayList<Object>();
            for (int i = 0; i < users.length; i++) {
                if (!users[i].toString().contains("Gateway")) {

                    ordered.add(users[i].toString().split("\\|")[0].replaceAll("\\s+", "")); //+ servers as indicator for Erlang to add "\n" in its place so that Java can read username later

                } else {
                    gateway = users[i].toString().split("\\|")[0].replaceAll("\\s+", "");
                }
            }
            if (!gateway.equals("")) {
                ordered.add(gateway);
            }


            outToServer.writeUTF(message + "!^!" + ordered.toString().replace('[', ' ').replace(']', ' ').replaceAll("\\s+", "") + "!?!" + "TEACHER\n"); // send it to server
            request = inRequest.readLine(); // Ask to send the file

            fromServ = inFromServer.readLine(); // receive teacher usernames
            System.out.println("From server=" + fromServ);
            String[] artest = fromServ.substring(0, fromServ.length() - 4).split(",");
            List teacherN = new LinkedList<String>(Arrays.asList(artest));
            System.out.println("Teacher usernames: " + teacherN.toString());
            teacherN.remove(0);
            System.out.println("Teacher usernames: " + teacherN.toString());
            teacherUsername = teacherN.toString(); // teacher usernames
            System.out.println("Teacher username: " + teacherUsername);
            fromServ = teacherN.toString();
            clientSocket.close();
            System.out.println("Socket closed!");
        } else {
            SocketAddress sockaddr = new InetSocketAddress(ip, 8080);
            Socket clientSocket = new Socket();
            clientSocket.connect(sockaddr, 5000);
            System.out.println(clientSocket);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            // here we receive msg from serverl
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            outToServer.writeUTF("GET" + '\n'); // Send the request
            fromServ = inFromServer.readLine(); // Receive the initial info from server

            System.out.println("FROM SERVER: " + fromServ);
            String[] temp = fromServ.split(Pattern.quote("*")); // split initial message from server to student username and other info (map, messages etc).
            System.out.println(Arrays.toString(temp));
            studentUsername = temp[1];
            System.out.println("Students username: " + studentUsername);
            fromServ = temp[0];
            clientSocket.close();
            System.out.println("Socket closed!");

        }
        return fromServ;
    }

    /**
     * Method to send diagram messages to server
     * sending can be either message itself, or confirmation
     * (sent by recipient when getting the message)
     *
     * @param message
     * @param confirm
     * @throws UnknownHostException
     * @throws IOException
     */
    public static void sendMessage(String message, boolean confirm) throws UnknownHostException, IOException {
        Socket clientSocket = new Socket(globalIP, 8080);
        System.out.println(clientSocket);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        if (!confirm) {
            outToServer.writeUTF(message + "!^!SEND!?!STUDENT" + '\n'); // Send the request
        } else {
            outToServer.writeUTF(message + "!^!CONFIRM!?!STUDENT" + '\n');
        }
        clientSocket.close();

    }

    public static void searchMeassage(ArrayList<? extends Object> message) throws IOException {
        Socket clientSocket = new Socket(globalIP, 8080);
        System.out.println(clientSocket);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        String sendToServer = message.toString();
        outToServer.writeUTF(sendToServer + "!?!SEARCH" + '\n');
        clientSocket.close();

    }

}
