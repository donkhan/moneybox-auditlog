package harmoney.auditlog.model;

import harmoney.auditlog.server.LogInserter;

import java.util.StringTokenizer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

public class AuditLog {
	
	@Id
    private String id;

    private long time;
    private String user;
    private String message;
    private String branch;
    private String module;
    
    public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branchName) {
		this.branch = branchName;
	}

	private String status;

    public AuditLog() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString(){
		return "Time " + time + " Message " + message + " Status " + status + " Branch  " + branch + " User " + user;
	}
	
	final static Logger logger = LoggerFactory.getLogger(LogInserter.class);
	
	public static AuditLog getLog(String message){
		logger.info("Going to Log {} ",message);
		AuditLog al = new AuditLog();
		al.setId(UUID.randomUUID().toString());
		StringTokenizer tokenizer = new StringTokenizer(message,":");
		al.setTime(Long.parseLong(getNextToken(tokenizer,Long.class)));
		al.setUser(getNextToken(tokenizer,String.class));
		al.setBranch(getNextToken(tokenizer,String.class));
		al.setModule(getNextToken(tokenizer,String.class));
		al.setMessage(getNextToken(tokenizer,String.class));
		al.setStatus(getNextToken(tokenizer,String.class));
		return al;
	}
	
	private static String getNextToken(StringTokenizer tokenizer, Object dataType){
		if(tokenizer.hasMoreTokens()){
			return tokenizer.nextToken();
		}
		if(dataType.equals(Long.class) || dataType.equals(Integer.class)){
			return "0";
		}
		return "";
		
	}
}
