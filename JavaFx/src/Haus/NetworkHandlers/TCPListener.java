package Haus.NetworkHandlers;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

import Haus.TechnicalFramework.Controllers.AnimationController;
import javafx.util.Pair;

public class TCPListener extends Thread {
    public static String[] srcDest = new String[]{"u1", "u2"};


    /*public static void main(String args[]) throws Exception {
        listen();
    }*/
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

            String check = serverSentence.substring (serverSentence.length () - 4, serverSentence.length ());
            System.out.println ("Ident: " + (check));
            if (!check.equals ("!US!")) {
                String toSend = serverSentence.substring (0, serverSentence.indexOf (','));
                System.out.println ("Here");
                connectionSocket.close ();
                System.out.println ("Sending confirmation ID: " + toSend);
                srcDest = getSenderRecipient (serverSentence);
                try {
                    Integer.parseInt (toSend);
                    TCPClient.sendMessage (toSend, true);
                } catch (Exception e) {
                    //do nothing
                }
                //AnimationController.x = srcDest[0];
                //AnimationController.y = srcDest[1];
                AnimationController.doAnimate = true;
                //AnimationController.runDjikstra();
            } else {
                System.out.println ("Teacher stuff: " + (serverSentence.substring (0, serverSentence.length () - 5).split (",")));
                //  TCPClient.teacherUsername=
            }
        }
    }

    public static String[] getSenderRecipient (String message) {
        String sender = message.substring (message.indexOf ("{") + 2, message.indexOf ("send") - 3);
        String recipient = message.substring (message.indexOf ("to") + 3, message.indexOf ("the") - 3);
        System.out.println ("Sender:" + sender + "  Recipient:" + recipient);
        String[] res = {sender, recipient};
        return res;
    }
}
