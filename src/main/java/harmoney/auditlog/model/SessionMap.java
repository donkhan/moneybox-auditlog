package harmoney.auditlog.model;

import java.util.HashMap;
import java.util.Map;

public class SessionMap extends HashMap<String,LoggedInUser> {
	
	private static final long serialVersionUID = 1L;
	private static SessionMap sessionMap;
	private static Map<String,String> reverseMap = new HashMap<String,String>();
	private SessionMap(){
		super();
	}
	
	public static SessionMap getSessionMap(){
		if(sessionMap == null){
			sessionMap = new SessionMap();
		}
		return sessionMap;
	}
	
	public LoggedInUser put(String sessionId,LoggedInUser u){
		if(reverseMap.containsKey(u.getName())){
			String id = reverseMap.get(u.getName());
			remove(id);
		}
		reverseMap.put(u.getName(),sessionId);
		return super.put(sessionId,u);
	}
}
