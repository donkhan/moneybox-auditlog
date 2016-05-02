package harmoney.auditlog.server;

import harmoney.auditlog.repository.AuditLogRepository;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditServer {
	private final static int PACKETSIZE = 1000;
	private AuditLogRepository auditLogRepo;
	
	final Logger logger = LoggerFactory.getLogger(AuditServer.class);
	
	public void start(int port,AuditLogRepository auditLogRepo) {
		this.auditLogRepo = auditLogRepo;
		//auditLogRepo.deleteAll();
		logger.info("No of Documents " , auditLogRepo.count());
		try {
			DatagramSocket socket = new DatagramSocket(port);
			logger.info("Audit Log Server is ready and listening in port " + port);
			for (;;) {
				DatagramPacket packet = new DatagramPacket(	new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(packet);
				//socket.send(packet);
				LogInserter li = new LogInserter(this.auditLogRepo,packet);
				li.start();
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}
}