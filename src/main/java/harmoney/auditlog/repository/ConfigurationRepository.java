package harmoney.auditlog.repository;

import harmoney.auditlog.model.Configuration;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigurationRepository 
	extends MongoRepository<Configuration,String>{
	
}
