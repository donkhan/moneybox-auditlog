package harmoney.auditlog.web;


import harmoney.auditlog.model.AuditLog;
import harmoney.auditlog.repository.AuditLogRepository;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditLogController {

    @RequestMapping("/")
    @CrossOrigin
    public Response index() {
    	String result = "This Micro Service will provide audit logs of the user activity in Money Box";
        return Response.ok().entity(result).header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
    }
    
    @Autowired
	private AuditLogRepository auditLogRepository;
    
    @RequestMapping("/get-logs")
    @CrossOrigin
    public Response getLogs(){
    	List<AuditLog> result = new ArrayList<AuditLog>();
    	result = auditLogRepository.findAll();
    	return Response.ok().entity(result).header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
    }

}