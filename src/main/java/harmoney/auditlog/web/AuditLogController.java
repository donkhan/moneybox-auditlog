package harmoney.auditlog.web;


import harmoney.auditlog.model.AuditLog;
import harmoney.auditlog.model.Page;
import harmoney.auditlog.repository.AuditLogRepository;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @RequestMapping("/get-audit-logs")
    @CrossOrigin
    public Response getAuditLogs(HttpServletRequest request){
    	System.out.println("Get Audit Logs");
    	Query query = getQuery(request);
    	
    	long count = mongoTemplate.count(query, AuditLog.class);
    	List<AuditLog> result = mongoTemplate.find(query, AuditLog.class);
    	Page page = createPage(count,result);
    	
    	System.out.println("No of Entries " + result.size());
    	return Response.ok().entity(page).header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
    }
    
    private Page createPage(long total,List<AuditLog> result){
    	Page page = new Page();
    	page.setTotal(total);
    	page.setContent(result);
    	return page;
    }
    
    private Query getQuery(HttpServletRequest request){
    	Query query = new Query();
    	int branchId = Integer.parseInt(request.getParameter("branch"));
    	long from = Long.parseLong(request.getParameter("from"));
    	long to = Long.parseLong(request.getParameter("to"));
    	String user = request.getParameter("user");
    	Criteria c = Criteria.where("time").gte(from).lte(to);
    	if(branchId != -1){
    		Criteria b = Criteria.where("branchId").is(branchId);
    		c.andOperator(b);
    	}
    	if("ALL".equals(user)){
    		Criteria u = Criteria.where("user").is(user);
    		c.andOperator(u);
    	}
    	//query.addCriteria(c);
    	return query;
    }

}