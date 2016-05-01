package harmoney.auditlog.server;

import java.util.StringTokenizer;
import java.util.UUID;

import harmoney.auditlog.model.AuditLog;
import harmoney.auditlog.repository.AuditLogRepository;

public class LogInserter extends Thread implements Runnable{
	
	private AuditLogRepository repo;
	private String message;
	
	public LogInserter(AuditLogRepository repo,String message){
		this.repo = repo;
		this.message = message;
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
