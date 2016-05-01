package harmoney.auditlog.server;

import harmoney.auditlog.model.AuditLog;
import harmoney.auditlog.repository.AuditLogRepository;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;
import java.util.UUID;

public class AuditServer {
	private final static int PACKETSIZE = 1000;
	private AuditLogRepository auditLogRepo;

	public void start(int port,AuditLogRepository auditLogRepo) {
		this.auditLogRepo = auditLogRepo;
		auditLogRepo.deleteAll();
		try {
			DatagramSocket socket = new DatagramSocket(port);
			System.out.println("Audit Log Server is ready and listening in port " + port);
			for (;;) {
				DatagramPacket packet = new DatagramPacket(	new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(packet);
				String message = new String(packet.getData());
				message = message.trim();
				System.out.println(packet.getAddress() + " " + packet.getPort()
						+ ": " + message);
				//socket.send(packet);
				LogInserter li = new LogInserter(this.auditLogRepo,message);
				li.start();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	

}