package harmoney.auditlog;

import harmoney.auditlog.model.Configuration;
import harmoney.auditlog.repository.AuditLogRepository;
import harmoney.auditlog.repository.ConfigurationRepository;
import harmoney.auditlog.server.AuditServer;
import harmoney.auditlog.server.RegistrationServer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner{
	
	@Autowired
	private AuditLogRepository auditLogRepository;
	
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	
    public static void main(String[] args) {
    	SpringApplication.run(App.class, args);
    	
    }
    
    final Logger logger = LoggerFactory.getLogger(App.class);
    
    private Configuration getAppConfiguration(){
    	List<Configuration> list = configurationRepository.findAll();
    	if(list.size() == 0){
    		logger.info("Server Ports are not configured. Going to use 5678 for Audit and 3332 for Registration. Please use Application Settings to change that in case you want.After that Restart me");
    		Configuration configuration = new Configuration();
    		configuration.setAuditServerPort(5678);
    		configuration.setRegistrationServerPort(3332);
    		return configuration;
    	}
    	return list.get(0);
    }
    
    @Override
	public void run(String... args) throws Exception {
    	logger.info("Configuration Repository {}",configurationRepository);
    	Configuration c = getAppConfiguration();
    	Thread server = new AuditServer(c.getAuditServerPort(),auditLogRepository);
    	server.start();
    	RegistrationServer registrationServer = new RegistrationServer(c);
    	registrationServer.start();
    }
    
    
}