package harmoney.auditlog.web;

import harmoney.auditlog.model.AuditLog;
import harmoney.auditlog.model.Page;
import harmoney.auditlog.repository.AuditLogRepository;

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
    	logger.info("Get Audig Logs called");
    	String requesterBranchName = request.getParameter("requester-branch");
    	String requesterName = request.getParameter("requester-name");
    	if(requesterName == null || requesterBranchName == null){
    		logger.error("Unable to serve as requester's name {} and branch Name {} ",requesterName,requesterBranchName );
    		return Response.serverError().build();
    	}
    	
    	Query query = getQuery(request);
    	auditLogRepository.save(AuditLog.getLog(""));
    	
    	long count = mongoTemplate.count(query, AuditLog.class);
    	List<AuditLog> result = mongoTemplate.find(query, AuditLog.class);
    	Page page = createPage(count,result);
    	
    	logger.info("No of Entries {} " ,count);
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
    	String branchName = request.getParameter("branch");
    	long from = Long.parseLong(request.getParameter("from"));
    	long to = Long.parseLong(request.getParameter("to"));
    	String user = request.getParameter("user");
    	Criteria c = Criteria.where("time").gte(from).lte(to);
    	logger.trace("From {} To {}", from , to);
    	logger.trace("User {} Branch {} ", user, branchName);
    	
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