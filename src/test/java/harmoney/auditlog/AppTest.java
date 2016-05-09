package harmoney.auditlog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import crawl.DClient;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	protected void syslog(String user, String message, String status) {
		for (int i = 0; i < 1000; i++) {
			System.out.println("Going to send SysLog");
			message = "MESSAGE " + i;
			String syslogMessage = System.currentTimeMillis() + ":" + user
					+ ":" + "MADURAI" + ":TELLERTRANSFER:" + message + ":" + status;
			try {
				DatagramSocket clientSocket = new DatagramSocket();
				InetAddress IPAddress = InetAddress.getByName("localhost");
				byte[] sendData = syslogMessage.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, IPAddress, 5678);
				clientSocket.send(sendPacket);
				clientSocket.close();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException ukhe) {
				ukhe.printStackTrace();

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			System.out.println("SysLog Sent");
		}
	}

	public static void main(String args[]) {
		new DClient().syslog("KHAN", "Logged In", "SUCCESS");
	}
}
