package Haus.NetworkHandlers;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

import Haus.TechnicalFramework.Controllers.AnimationController;
import javafx.util.Pair;

public class TCPListener extends Thread {
	public static int allowMessage =0; //priority counter
    public static String[] srcDest;
    public static String messageReceiveLog=""; // for displaying message log in main window

    /**
     * Method for listening to the server to get incoming messages
     * and (for teacher only) username updates
     * @throws IOException
     */
    public static void listen () throws IOException {

        String serverSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket (6789);


        while (true) {
            Socket connectionSocket = welcomeSocket.accept ();
            BufferedReader inFromClient =
                    new BufferedReader (new InputStreamReader (connectionSocket.getInputStream ()));
            DataOutputStream outToClient = new DataOutputStream (connectionSocket.getOutputStream ());
            serverSentence = inFromClient.readLine ();
            System.out.println ("Received: " + (serverSentence));

            String check = serverSentence.substring (serverSentence.length () - 4, serverSentence.length ()); // check if its teacher username update
            System.out.println ("Ident: " + (check)); 
            if (!check.equals ("!US!")) { //if incoming transmission is not teacher username update
                String toSend = serverSentence.substring (0, serverSentence.indexOf (','));
                System.out.println ("Here");
                connectionSocket.close ();
                System.out.println ("Sending confirmation ID: " + toSend);
                srcDest = getSenderRecipient (serverSentence);
                try {
                    Integer.parseInt (toSend); // try to send confirmation to server, if its message with ID
                    TCPClient.sendMessage (toSend, true);
                } catch (Exception e) {
               	 allowMessage++; //update priority counter to allow sending more messages
               	 System.out.println("counter " + allowMessage);
                    messageReceiveLog=messageReceiveLog+serverSentence+"\n"; // display message in log if its message without ID
                    // animation method probably has to be here 
                }
                //AnimationController.x = srcDest[0];
                //AnimationController.y = srcDest[1];
                AnimationController.doAnimate = true;
                //AnimationController.runDjikstra();
            } else {
                System.out.println ("Teacher stuff: " + (serverSentence.substring (0, serverSentence.length () - 4).split (",")));
                TCPClient.teacherUsername=Arrays.toString(serverSentence.substring (0, serverSentence.length () - 4).replaceFirst(",", "").trim().split (",")); // update teacher username list
            }
        }
    }
    
 /**
  * Method to get teacher and username from the message
  * @param message
  * @return
  */
    public static String[] getSenderRecipient (String message) {
        String sender = message.substring (message.indexOf ("{") + 2, message.indexOf ("send") - 3);
        String recipient = message.substring (message.indexOf ("to") + 3, message.indexOf ("the") - 3);
        System.out.println ("Sender:" + sender + "  Recipient:" + recipient);
        String[] res = {sender, recipient};
        return res;
    }
}
