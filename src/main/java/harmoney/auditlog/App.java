package harmoney.auditlog;

import harmoney.auditlog.server.AuditServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
    	SpringApplication.run(App.class, args);
    	AuditServer server = new AuditServer();
    	server.start(5678);
    }

}