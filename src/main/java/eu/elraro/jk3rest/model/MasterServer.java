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
			if (status == ServerResponseStatus.OK) {
				//protocol.updateMasterInfo(this);
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
