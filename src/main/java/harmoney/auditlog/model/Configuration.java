package harmoney.auditlog.model;

public class Configuration {

	private int auditServerPort;
	private int registrationServerPort;
	public int getAuditServerPort() {
		return auditServerPort;
	}
	public void setAuditServerPort(int auditServerPort) {
		this.auditServerPort = auditServerPort;
	}
	public int getRegistrationServerPort() {
		return registrationServerPort;
	}
	public void setRegistrationServerPort(int registrationServerPort) {
		this.registrationServerPort = registrationServerPort;
	}
	
	
}
