package harmoney.auditlog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

/**
 * Unit test for simple App.
 */
public class AppTest {

	@SuppressWarnings("unchecked")
	protected void syslog(String user, String message, String status) {

		System.out.println("Going to send SysLog");
		JSONObject sysLogPayLoad = new JSONObject();
		sysLogPayLoad.put("time", System.currentTimeMillis());
		sysLogPayLoad.put("user", user);
		sysLogPayLoad.put("branch", "MADURAI");
		sysLogPayLoad.put("module", "TELLERTRANSFER");
		sysLogPayLoad.put("status", status);
		sysLogPayLoad.put("message", "HHAHA");
		sysLogPayLoad.put("message-type", "LOG");

		String syslogMessage = sysLogPayLoad.toJSONString();
		System.out.println("Syslog message " + syslogMessage);
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

	@SuppressWarnings("unchecked")
	protected void controlMessages(String user, String sessionId,
			String branchName, String type) {

		System.out.println("Going to send Control logs");
		JSONObject sysLogPayLoad = new JSONObject();
		sysLogPayLoad.put("user", user);
		sysLogPayLoad.put("branch", branchName);
		sysLogPayLoad.put("session-id", sessionId);
		sysLogPayLoad.put("message-type", type);

		String syslogMessage = sysLogPayLoad.toJSONString();
		System.out.println("Syslog message " + syslogMessage);
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
		System.out.println("Control Message Sent");
	}

	public static void main(String args[]) {
		AppTest appTest = new AppTest();
		appTest.syslog("KHAN", "Logged In", "SUCCESS");
		appTest.controlMessages("KKHAN", "S1", "MADURAI", "LOG IN");

	}
}
