package eu.elraro.jk3rest.model;

import java.util.ArrayList;

import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;

public class MasterServer {

	private int port;
	private String ipAddress;
	private boolean online;
	
	public MasterServer() {}

	public MasterServer(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public ServerResponseStatus connect(Quake3Protocol protocol) {
		if (protocol.connect(this.ipAddress, this.port) == ServerResponseStatus.CONNECTED) {
			this.ipAddress = protocol.getIpAddress().getHostAddress();
			ServerResponseStatus status = protocol.query("getservers 26 empty full");
			ArrayList<String> servers = new ArrayList<String>();
			if (status == ServerResponseStatus.OK) {
//				System.out.println(protocol.response);
//				for(int i = 0; i < protocol.response.length()-10; i++) {
//					if(protocol.response.charAt(i) == '\\' && protocol.response.charAt(i+7) == '\\') {
//						String ip = (int) protocol.response.charAt(i+1) + "." + (int) protocol.response.charAt(i+2) + "." + (int) protocol.response.charAt(i+3) + "." + (int) protocol.response.charAt(i+4);
//						int port = ((int) protocol.response.charAt(i+5) << 8) + (int) protocol.response.charAt(i+6);
//						//if (!ip.contains("65533")) {
//							servers.add(ip + ":" + port);
//						//}
//					}
//				}
//				System.out.println(servers);
			}
		}
		return protocol.getResponseStatus();
	}
	
	public boolean getOnline() {
		return this.online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
}
