package harmoney.auditlog.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class AuditServer {
	private final static int PACKETSIZE = 1000;
	public void start(int port) {
		try {
			DatagramSocket socket = new DatagramSocket(port);
			System.out.println("Audit Log Server is ready...");
			for (;;) {
				DatagramPacket packet = new DatagramPacket(	new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(packet);
				String message = new String(packet.getData());
				message = message.trim();
				System.out.println(packet.getAddress() + " " + packet.getPort()
						+ ": " + message);
				//socket.send(packet);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}