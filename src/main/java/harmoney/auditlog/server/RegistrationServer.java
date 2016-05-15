package harmoney.auditlog.server;

import harmoney.auditlog.model.LoggedInUser;
import harmoney.auditlog.model.SessionMap;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrationServer {
	private final static int PACKETSIZE = 10000;
	
	final Logger logger = LoggerFactory.getLogger(RegistrationServer.class);
	private JSONParser parser = new JSONParser();
	
	public void start(int port) {
		try {
			DatagramSocket socket = new DatagramSocket(port);
			logger.info("Registration Server is ready and listening in port " + port);
			for (;;) {
				DatagramPacket packet = new DatagramPacket(	new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(packet);
				String data = new String(packet.getData());
				JSONObject jsonContent = getJSONObject(data.trim());
				LoggedInUser u = new LoggedInUser();
				u.setName((String)jsonContent.get("id"));
				u.setRole((String)jsonContent.get("roleId"));
				JSONObject branch = (JSONObject)jsonContent.get("branch");
				u.setBranchName((String)branch.get("name"));
				
				logger.info("{}",u);
				
				InetAddress IPAddress = packet.getAddress();
                int receivedPort = packet.getPort();
                String token = UUID.randomUUID().toString();
                SessionMap.getSessionMap().put(token,u);
                DatagramPacket sendPacket =  new DatagramPacket(token.getBytes(), token.getBytes().length,
                		IPAddress, receivedPort);
                socket.send(sendPacket);
                
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	private JSONObject getJSONObject(String message){
		logger.info("Message {}",message);
		try {
			JSONObject jsonObject = (JSONObject)parser.parse(message);
			return jsonObject;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}