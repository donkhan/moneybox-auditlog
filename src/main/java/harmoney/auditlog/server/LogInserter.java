package harmoney.auditlog.server;

import harmoney.auditlog.model.AuditLog;
import harmoney.auditlog.repository.AuditLogRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogInserter extends Thread implements Runnable{
	
	private AuditLogRepository repo;
	final Logger logger = LoggerFactory.getLogger(LogInserter.class);
	private List<String> messageList;
	
	public LogInserter(AuditLogRepository repo,List<String> messageList){
		this.repo = repo;
		this.messageList = messageList;
	}
	
	@Override
	public void run() {
		while(true){
			if(messageList.size() == 0){
				logger.info("Since Message List is empty i am going to sleep for 5 seconds");
				try {
					Thread.sleep(5*1000);
				} catch (InterruptedException e) {
					logger.error("Error {}",e);
				}
			}else{
				repo.save(AuditLog.getLog((messageList.remove(0))));
			}
		}
	} 
	
}
