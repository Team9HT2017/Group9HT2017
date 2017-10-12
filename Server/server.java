//-------------------------------------------------------------------
// @author LFigueroa
// @copyright (C) 2017, <HAUS PROJECT>
// @doc
//
// @end
// Created : 11. Oct 2017 10:24
//-------------------------------------------------------------------
package Haus;

import com.ericsson.otp.erlang.OtpSelf;
import java.net.InetAddress;


public class server {

	private OtpSelf client;
	private OtpPeer server;
	private OtpConnection connection;

	@Before
	public void init() throws IOException, OtpAuthException {
		client = new OtpSelf("client", "house");
		server = new OtpPeer("server@Nick-HP"); // I think we need some change here!
		// Like to identify which computer is the server each time
		connection = client.connect(server);
	}

	@Test
	public void shouldInvokeErlangTranslateFunction() throws IOException,
			OtpAuthException, OtpErlangExit, OtpErlangDecodeException {
		
		connection.sendRPC("server", "start_link", withArgs());
		OtpErlangObject response = connection.receiveMsg().getMsg();
		assertTrue(response.toString().contains());
	}

	private OtpErlangObject[] withArgs(String word, String language) { // need to be changed, I do not know what now
		return new OtpErlangObject[] { 
				new OtpErlangAtom(word),
				new OtpErlangAtom(language)
		};
	}
}
class IP_address
{
	public static void main(String args[]) throws Exception
	{
		IP ip = InetAddress.getLocalHost()); // returns an object
	}
}