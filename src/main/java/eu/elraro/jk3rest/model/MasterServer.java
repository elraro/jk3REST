package eu.elraro.jk3rest.model;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;

@Entity
public class MasterServer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private int port;
	private String ipAddress;
	private boolean online;
	private ArrayList<String> servers;
	
	public MasterServer() {}

	public MasterServer(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.online = false;
		this.setServers(new ArrayList<String>());
	}

	public ServerResponseStatus connect(Quake3Protocol protocol) {
		if (protocol.connect(this.ipAddress, this.port) == ServerResponseStatus.CONNECTED) {
			this.ipAddress = protocol.getIpAddress().getHostAddress();
			ServerResponseStatus status = protocol.query("getservers 26 empty full");
			if (status == ServerResponseStatus.OK) {
				protocol.updateMasterInfo(this);
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

	public ArrayList<String> getServers() {
		return servers;
	}

	public void setServers(ArrayList<String> servers) {
		this.servers = servers;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
