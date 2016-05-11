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
		for (int i = 0; i < 1; i++) {
			System.out.println("Going to send SysLog");
			message = "MESSAGE " + i;
			JSONObject sysLogPayLoad = new JSONObject();
			sysLogPayLoad.put("time",System.currentTimeMillis());
			sysLogPayLoad.put("user",user);
			sysLogPayLoad.put("branch","MADURAI");
			sysLogPayLoad.put("module","TELLERTRANSFER");
			sysLogPayLoad.put("status",status);
			sysLogPayLoad.put("message","HHAHA");
			
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
	}

	public static void main(String args[]) {
		new AppTest().syslog("KHAN", "Logged In", "SUCCESS");
	}
}
