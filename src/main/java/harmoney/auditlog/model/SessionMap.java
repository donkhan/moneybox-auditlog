package harmoney.auditlog.model;

import java.util.HashMap;

public class SessionMap extends HashMap<String,LoggedInUser> {
	
	private static final long serialVersionUID = 1L;
	private static SessionMap sessionMap;
	private SessionMap(){
		super();
	}
	
	public static SessionMap getSessionMap(){
		if(sessionMap == null){
			sessionMap = new SessionMap();
		}
		return sessionMap;
	}
	
}
