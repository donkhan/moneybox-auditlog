package harmoney.auditlog.repository;

import harmoney.auditlog.model.AuditLog;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository 
	extends MongoRepository<AuditLog,String>{
	
}
