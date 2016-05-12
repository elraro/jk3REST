package eu.elraro.jk3rest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;

@Entity
public class GameServer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private int currentClients, port, ping, maxClients;
	private String ipAddress, mapName, hostName, coloredHostName;
	private boolean isOnline, isPasswordProtected;
	@OneToMany(cascade=CascadeType.ALL)
	private List<Player> players;
	@Column(length=5000)
	private HashMap<String, String> parameters;
	private long timestamp;
	
	public GameServer() {}

	public GameServer(String ipAddress, int port, long timestamp) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.timestamp = timestamp;
		this.players = new ArrayList<Player>();
		this.parameters = new HashMap<String, String>();
	}

	public ServerResponseStatus connect(Quake3Protocol protocol) {
		if (protocol.connect(this.ipAddress, this.port) == ServerResponseStatus.CONNECTED) {
			this.ipAddress = protocol.getIpAddress().getHostAddress();
			ServerResponseStatus status = protocol.query("getstatus");
			if (status == ServerResponseStatus.OK) {
				protocol.updateServerInfo(this);
			}
		}
		return protocol.getResponseStatus();
	}

	public boolean isOnline() {
		return this.isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public HashMap<String, String> getParameters() {
		return this.parameters;
	}

	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPing() {
		return this.ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public String getColoredHostName() {
		return this.coloredHostName;
	}

	public void setColoredHostName(String coloredHostName) {
		this.coloredHostName = coloredHostName;
	}

	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getMapName() {
		return this.mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public boolean isPasswordProtected() {
		return isPasswordProtected;
	}

	public void setPasswordProtected(boolean isPasswordProtected) {
		this.isPasswordProtected = isPasswordProtected;
	}

	public int getMaxClients() {
		return this.maxClients;
	}

	public void setMaxClients(int maxClients) {
		this.maxClients = maxClients;
	}

	public int getCurrentClients() {
		return this.currentClients;
	}

	public void setCurrentClients(int currentClients) {
		this.currentClients = currentClients;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "GameServer [currentClients=" + currentClients + ", port=" + port + ", ping="
				+ ping + ", maxClients=" + maxClients + ", ipAddress=" + ipAddress + ", mapName=" + mapName
				+ ", hostName=" + hostName + ", coloredHostName=" + coloredHostName + ", isOnline=" + isOnline
				+ ", isPasswordProtected=" + isPasswordProtected + ", players=" + players + "]";
	}
}
