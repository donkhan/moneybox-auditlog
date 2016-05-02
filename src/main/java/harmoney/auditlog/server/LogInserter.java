package harmoney.auditlog.server;

import harmoney.auditlog.model.AuditLog;
import harmoney.auditlog.repository.AuditLogRepository;

import java.net.DatagramPacket;
import java.util.StringTokenizer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogInserter extends Thread implements Runnable{
	
	private AuditLogRepository repo;
	private String message;
	final Logger logger = LoggerFactory.getLogger(LogInserter.class);
	
	public LogInserter(AuditLogRepository repo,DatagramPacket packet){
		String message = new String(packet.getData());
		logger.trace("Address {} Port {} Message {}",packet.getAddress(), packet.getPort(), message);
		this.repo = repo;
		this.message = message.trim();
	}
	
	@Override
	public void run() {
		log(this.message);
	}
	
	private void log(String message){
		AuditLog al = new AuditLog();
		al.setId(UUID.randomUUID().toString());
		StringTokenizer tokenizer = new StringTokenizer(message,":");
		al.setTime(Long.parseLong(tokenizer.nextToken()));
		al.setUser(tokenizer.nextToken());
		al.setBranch(tokenizer.nextToken());
		al.setMessage(tokenizer.nextToken());
		al.setStatus(tokenizer.nextToken());
		repo.save(al);
	}
}
