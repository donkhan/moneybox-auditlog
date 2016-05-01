package harmoney.auditlog.model;

import org.springframework.data.annotation.Id;

public class AuditLog {
	
	@Id
    private String id;

    private long time;
    private String user;
    private String message;
    private long branchId;
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

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString(){
		return "Time " + time + " Message " + message + " Status " + status + " Branch ID " + branchId + " User " + user;
	}
}
