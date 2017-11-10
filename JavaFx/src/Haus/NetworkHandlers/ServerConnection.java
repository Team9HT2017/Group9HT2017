package Haus.NetworkHandlers;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;
import com.ericsson.otp.erlang.*;


public class ServerConnection {
	static OtpErlangObject Reply = new OtpErlangAtom ("no_reply");
	public static String sendExp(String message, String command) throws IOException, OtpErlangExit, OtpErlangDecodeException{
	String sname = "erl2";//start shell with "erl -sname <name that you want> -setcookie warv", set recipient string to shell's address
    // should look like <name>@your computer name, for example: myShell@LAPTOP-5N3JD1
    String servername = "testServ"; //set it to name of Erlang server class that you're trying to connect to
    //It will be hardcoded when server will  be done
    OtpNode node = new OtpNode("javaside","warv"); //create node
    String recipient = sname+"@"+node.host();
    OtpMbox mailbox = node.createMbox("mailbox1"); //create mailbox to send and receive messages
    
        /*String mes2 = new String(message); // String splitting will be added later
        int coefficient = message.length() / 4 +1; //split message in parts shorter than 250 chars (erlang atom is 255 max)
        OtpErlangObject [] tt = new OtpErlangObject[coefficient];
        int k=coefficient;
        for (int i=0;i<coefficient;i++){
        	tt[i]= new OtpErlangAtom(mes2.substring(0,mes2.length()/k));
        	if (k!=0){
        	mes2=mes2.substring(mes2.length()/k,Math.min(mes2.length()/k, mes2.length()));}
        			k--;
        }*/
    
        if (message != null && !message.equals("nope")) { //send a map
            mailbox.send(servername, recipient,new OtpErlangTuple (new OtpErlangObject [] 
            		{new OtpErlangAtom("mailbox1"),new OtpErlangList(
            				new OtpErlangObject [] {new OtpErlangAtom(message.substring(0, message.length()/2)),new OtpErlangAtom(message.substring(message.length()/2,message.length()))}) //dissected map to send
            				,new OtpErlangAtom (command)}));
            Reply = mailbox.receive(500);
            if (Reply == null) {
                System.out.println("Reply took too long");
            } else {
                System.out.println("Server replied: "+ Reply.toString());
            }
        }
        else {  //send map request/stop command
        	
        	 mailbox.send(servername, recipient,new OtpErlangTuple (new OtpErlangObject [] 
             		{new OtpErlangAtom("mailbox1"),new OtpErlangAtom (command)}));
             
             Reply = mailbox.receive(500);
             if (Reply == null) {
                 System.out.println("Reply took too long");
             } else {
                 System.out.println("Server replied: "+ Reply.toString());
             }
        }
		return Reply.toString();
    }

	
}

