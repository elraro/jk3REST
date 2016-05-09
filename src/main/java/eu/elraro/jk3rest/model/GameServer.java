package eu.elraro.jk3rest.model;

import java.util.ArrayList;
import java.util.TreeMap;

import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;

public class GameServer {
	private Quake3Protocol protocol;

	private int currentClients, port, ping, maxClients;
	private String ipAddress, mapName, hostName, coloredHostName;
	private boolean isOnline, isPasswordProtected;
	private ArrayList<Player> players;
	private TreeMap<String, String> parameters;

	public GameServer(String ipAddress, int port, Quake3Protocol protocol) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.protocol = protocol;
		this.players = new ArrayList<>();
		this.parameters = new TreeMap<>();
	}

	public ServerResponseStatus connect() {
		if (this.protocol.connect(this.ipAddress, this.port) == ServerResponseStatus.CONNECTED) {
			this.ipAddress = this.protocol.getIpAddress().getHostAddress();
		}

		return this.protocol.getResponseStatus();
	}

	public void disconnect() {
		this.isOnline = false;
		this.protocol.disconnect();
	}

	public ServerResponseStatus update() {
		if (this.protocol.getResponseStatus() == ServerResponseStatus.CONNECTED) {
			if (this.protocol.query("getstatus") == ServerResponseStatus.OK) {
				this.protocol.updateServerInfo(this);
			}
		}
		return this.protocol.getResponseStatus();
	}

	public Quake3Protocol getProtocol() {
		return this.protocol;
	}

	public boolean isOnline() {
		return this.isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public TreeMap<String, String> getParameters() {
		return this.parameters;
	}

	public void setParameters(TreeMap<String, String> parameters) {
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

	@Override
	public String toString() {
		return "GameServer [protocol=" + protocol + ", currentClients=" + currentClients + ", port=" + port + ", ping="
				+ ping + ", maxClients=" + maxClients + ", ipAddress=" + ipAddress + ", mapName=" + mapName
				+ ", hostName=" + hostName + ", coloredHostName=" + coloredHostName + ", isOnline=" + isOnline
				+ ", isPasswordProtected=" + isPasswordProtected + ", players=" + players + "]";
	}

}
