import java.io.*;
import java.net.*;

class TCPClient {
        public static void main(String argv[]) throws Exception {
                String sentence;
                String modifiedSentence;
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                Socket clientSocket = new Socket(kindOfUser(), 8080); // sets the connection to an IP and a port
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                sentence = inFromUser.readLine();
                outToServer.writeBytes(sentence + '\n');
                modifiedSentence = inFromServer.readLine();
                System.out.println("FROM SERVER: " + modifiedSentence);
                clientSocket.close();
        }
        static kindOfUser(){

            if Controller.user == "professor" {
                InetAddress IP = InetAddress.getLocalHost(); // get the professor ip
                return IP.getHostAddress();
        }
        else {
                return Controller.IP.getText(); //get the ip typed on the first page
            }
        }
}