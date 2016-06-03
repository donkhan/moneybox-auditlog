package harmoney.auditlog.server;

import harmoney.auditlog.model.Configuration;
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
	
	private Configuration c;
	public RegistrationServer(Configuration c) {
		this.c = c;
	}

	public void start() {
		try {
			DatagramSocket socket = new DatagramSocket(c.getRegistrationServerPort());
			logger.info("Registration Server is ready and listening in port " + c.getRegistrationServerPort());
			for (;;) {
				DatagramPacket packet = new DatagramPacket(	new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(packet);
				String data = new String(packet.getData());
				JSONObject jsonContent = getJSONObject(data.trim());
				logger.info("{}",jsonContent);
				
				InetAddress IPAddress = packet.getAddress();
                int receivedPort = packet.getPort();
                String token = UUID.randomUUID().toString();
                SessionMap.getSessionMap().put(token,jsonContent);
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