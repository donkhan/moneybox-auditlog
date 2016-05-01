package harmoney.auditlog;

import harmoney.auditlog.repository.AuditLogRepository;
import harmoney.auditlog.server.AuditServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner{
	
	@Autowired
	private AuditLogRepository auditLogRepository;
	
    public static void main(String[] args) {
    	SpringApplication.run(App.class, args);
    	
    }
    
    @Override
	public void run(String... args) throws Exception {
    	AuditServer server = new AuditServer();
    	server.start(5678,auditLogRepository);
    }
    
    
}