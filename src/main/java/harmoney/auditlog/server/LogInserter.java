package harmoney.auditlog.server;

import harmoney.auditlog.model.AuditLog;
import harmoney.auditlog.repository.AuditLogRepository;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
	
	private JSONParser parser = new JSONParser();
	@Override
	public void run() {
		while(true){
			if(messageList.size() == 0){
				logger.trace("Since Message List is empty i am going to sleep for 5 seconds");
				try {
					Thread.sleep(5*1000);
				} catch (InterruptedException e) {
					logger.error("Error {}",e);
				}
			}else{
				JSONObject jsonContent = getJSONObject(messageList.remove(0).trim());
				logger.info("Log Message");
				repo.save(getLog(jsonContent));
			}
		}
	} 
	


	private JSONObject getJSONObject(String message){
		logger.info("Message {}",message);
		try {
			JSONObject jsonObject = (JSONObject)parser.parse(message);
			return jsonObject;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private AuditLog getLog(JSONObject jsonObject){
		AuditLog auditLog = new AuditLog();
		auditLog.setBranch(jsonObject.get("branch").toString());
		auditLog.setMessage(jsonObject.get("message").toString());
		auditLog.setModule(jsonObject.get("module").toString());
		auditLog.setUser(jsonObject.get("user").toString());
		auditLog.setStatus(jsonObject.get("status").toString());
		auditLog.setTime(Long.parseLong(jsonObject.get("time").toString()));
		return auditLog;
	}
	/*
	public static void main(String args[]){
		String message = "{\"module\":\"TELLERTRANSFER\",\"time\":1462950297064,\"status\":\"SUCCESS\",\"branch\":\"MADURAI\",\"user\":\"KHAN\",\"message\":\" Junk TELLERTRANSFER\"}";
		LogInserter li = new LogInserter(null,null);
		AuditLog log = li.getLog(message);
	}
	*/
}
