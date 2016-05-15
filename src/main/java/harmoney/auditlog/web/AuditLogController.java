package harmoney.auditlog.web;

import harmoney.auditlog.model.AuditLog;
import harmoney.auditlog.model.LoggedInUser;
import harmoney.auditlog.model.Page;
import harmoney.auditlog.model.SessionMap;
import harmoney.auditlog.repository.AuditLogRepository;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditLogController {

	final Logger logger = LoggerFactory.getLogger(AuditLogController.class);
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
    	logger.info("Get Audit Logs called");
    	String  token = request.getParameter("token");
    	SessionMap sessionMap = SessionMap.getSessionMap();
    	if(!sessionMap.containsKey(token)){
    		logger.error("Unable to serve as token {} is not present in Audit Log db",token);
    		return Response.serverError().build();
    	}
    	LoggedInUser user = sessionMap.get(token);
    	log(user);
    	Query query = getQuery(request);
    	long count = mongoTemplate.count(query, AuditLog.class);
    	List<AuditLog> result = mongoTemplate.find(query, AuditLog.class);
    	Page page = createPage(count,result);
    	
    	logger.info("No of Entries {} " ,count);
    	return Response.ok().entity(page).header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
    			.header("Access-Control-Allow-Headers", "x-userid").build();
    }
    
    private void log(LoggedInUser user) {
    	AuditLog auditLog = new AuditLog();
    	auditLog.setBranch(user.getBranchName());
    	auditLog.setUser(user.getName());
    	auditLog.setTime(System.currentTimeMillis());
    	auditLog.setModule("AUDIT LOG");
    	auditLog.setStatus("SUCCESS");
    	auditLog.setMessage("Retrieved Audit Logs");
    	auditLogRepository.save(auditLog);
	}

	private Page createPage(long total,List<AuditLog> result){
    	Page page = new Page();
    	page.setTotal(total);
    	page.setContent(result);
    	return page;
    }
    
    private Query getQuery(HttpServletRequest request){
    	Query query = new Query();
    	String branchName = request.getParameter("branch");
    	long from = Long.parseLong(request.getParameter("from"));
    	long to = Long.parseLong(request.getParameter("to"));
    	String user = request.getParameter("user");
    	Criteria c = Criteria.where("time").gte(from).lte(to);
    	logger.info("From {} To {}", from , to);
    	logger.info("User {} Branch {} ", user, branchName);
    	
    	if(!"ALL".equals(branchName) && !"ALL".equals(user)){
    		logger.trace("Specific User and Specific Branch Case (teller)");
    		Criteria b = Criteria.where("branch").is(branchName).andOperator(Criteria.where("user").is(user));
    		c.andOperator(b);
    	}
    	else if(!"ALL".equals(branchName)){
    		logger.trace("Specific Branch Case (manager)");
    		Criteria u = Criteria.where("branch").is(branchName);
    		c.andOperator(u);
    	}else{
    		logger.trace("All Case (sadmin)");
    	}
    	query.addCriteria(c);
    	query.with(new Sort(new Order(Direction.DESC, "time")));
    	
    	int pageNo = Integer.parseInt(request.getParameter("pageNo"));
    	int pageSize = Integer.parseInt(request.getParameter("pageSize"));
    	logger.info("Page No {}, Page Size {}",pageNo,pageSize);
    	
    	PageRequest pageRequest = new PageRequest(pageNo,pageSize);
    	query.with(pageRequest);
    	
    	return query;
    }
    
    
}