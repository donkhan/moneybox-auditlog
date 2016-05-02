package harmoney.auditlog.server;

import harmoney.auditlog.repository.AuditLogRepository;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditServer {
	private final static int PACKETSIZE = 1000;
	
	final Logger logger = LoggerFactory.getLogger(AuditServer.class);
	private List<String> messageList = new Vector<String>();
	
	public void start(int port,AuditLogRepository auditLogRepo) {
		
		LogInserter li = new LogInserter(auditLogRepo,messageList);
		li.start();
		
		logger.info("No of Existing AuditLogs {} " , auditLogRepo.count());
		try {
			DatagramSocket socket = new DatagramSocket(port);
			logger.info("Audit Log Server is ready and listening in port " + port);
			for (;;) {
				DatagramPacket packet = new DatagramPacket(	new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(packet);
				String data = new String(packet.getData());
				messageList.add(data.trim());
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}
}