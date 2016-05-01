package harmoney.auditlog.model;

import java.util.ArrayList;
import java.util.List;

public class Page {
	
	private long total;
	private List<AuditLog> content = new ArrayList<AuditLog>();
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<AuditLog> getContent() {
		return content;
	}
	public void setContent(List<AuditLog> content) {
		this.content = content;
	}
	
}
