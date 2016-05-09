package eu.elraro.jk3rest.query;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.elraro.jk3rest.model.GameServer;
import eu.elraro.jk3rest.model.Player;

public class Quake3Protocol {
	private DatagramSocket socket = null;
	private DatagramPacket packet = null;
	private Pattern pattern = null;

	private String response;
	private String ipHostName;
	private InetAddress ipAddress;
	private int port;
	private TreeMap<String, String> parameters;
	private int timeout = 1500;
	private ServerResponseStatus responseStatus;

	private long time;
	private long deltaTime;
	private static final String PLAYER_REGEX = "(\\d+) (\\d+) \"(.*)\"";

	public Quake3Protocol() {
		this.parameters = new TreeMap<String, String>();
		this.pattern = Pattern.compile(PLAYER_REGEX);
	}

	public ServerResponseStatus connect(String ip, Integer port) {
		try {
			this.ipAddress = InetAddress.getByName(ip);
			this.ipHostName = getIpAddress().getHostName();
			if (port != null)
				this.port = port;
			if (port < 0 || port > 65536)
				return responseStatus = ServerResponseStatus.ILLEGAL_ARGUMENT_EXCEPTION;
		} catch (UnknownHostException e) {
			return responseStatus = ServerResponseStatus.UNKNOWN_HOST_EXCEPTION;
		}

		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(timeout);
		} catch (SocketException e) {
			return responseStatus = ServerResponseStatus.SOCKET_EXCEPTION;
		}
		return responseStatus = ServerResponseStatus.CONNECTED;
	}

	public void disconnect() {
		if (socket != null) {
			socket.disconnect();
		}
	}

	public ServerResponseStatus query(String request) {
		byte[] tmp = request.getBytes();
		int offset = 4;
		byte[] buffer = new byte[tmp.length + offset];

		for (int i = 0; i < tmp.length + offset; i++) {
			if (i < offset)
				buffer[i] = (byte) 0xff; // oob
			else
				buffer[i] = tmp[i - offset];
		}

		packet = new DatagramPacket(buffer, buffer.length, getIpAddress(), port);
		try {
			time = System.currentTimeMillis();
			socket.send(packet);
		} catch (IOException e) {
			return responseStatus = ServerResponseStatus.IO_EXCEPTION;
		}

		response = getResponse();
		return getResponseStatus();
	}

	private String getResponse() {
		byte[] response = new byte[65507];
		packet = new DatagramPacket(response, response.length);
		try {
			socket.receive(packet);
			deltaTime = System.currentTimeMillis() - time;
			responseStatus = ServerResponseStatus.OK;
		} catch (SocketTimeoutException e) {
			responseStatus = ServerResponseStatus.SOCKET_TIMEOUT_EXCEPTION;
			return null;
		} catch (IOException e) {
			responseStatus = ServerResponseStatus.IO_EXCEPTION;
			return null;
		}

		return new String(response);
	}

	public ServerResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void updateServerInfo(GameServer server) {
		if (response == null || response.contains("disconnect")) {
			server.setOnline(false);
			return;
		}

		server.setPlayers(new ArrayList<Player>());
		String[] lines = response.split("\\n");

		//System.out.println(lines[1]);

		// parameters
		StringTokenizer tokens = new StringTokenizer(lines[1], "\\");
		while (tokens.hasMoreTokens()) {
			String key = tokens.nextToken();
			String value = tokens.nextToken();
			parameters.put(key, value);
		}

		// players
		for (int i = 2; i < lines.length - 1; i++) {
			if (lines[i].length() == 0)
				continue;
			server.getPlayers().add(parsePlayer(lines[i]));
			//System.out.println(server.getPlayers().get(i - 2));
		}

		updateParameters(server);
	}

	private void updateParameters(GameServer server) {
		server.setParameters(parameters);

		server.setOnline(true);
		server.setIpAddress(getIpAddress().getHostAddress());
		server.setPort(port);
		server.setPing((int) deltaTime);

		server.setColoredHostName(Normalizer.normalize(parameters.get("sv_hostname"), Normalizer.Form.NFD).replace("\uFFFD", ""));
		server.setHostName(Normalizer.normalize(Utilities.removeColorCode(server.getColoredHostName()), Normalizer.Form.NFC).replace("\uFFFD", ""));
		server.setMapName(parameters.get("mapname"));
		server.setPasswordProtected(Boolean.parseBoolean(parameters.get("g_needpass")));
		server.setMaxClients(Integer.parseInt(parameters.get("sv_maxclients")));
		server.setCurrentClients(server.getPlayers().size());
	}

	private Player parsePlayer(String line) {
		Matcher matcher = pattern.matcher(line);

		if (matcher.find()) {
			int score = Integer.parseInt(matcher.group(1));
			int ping = Integer.parseInt(matcher.group(2));
			String coloredName = Normalizer.normalize(matcher.group(3), Normalizer.Form.NFD).replace("\uFFFD", "");
			String name = Normalizer.normalize(Utilities.removeColorCode(coloredName), Normalizer.Form.NFD).replace("\uFFFD", "");
			return new Player(name, coloredName, score, ping);
		}
		return null;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}
}
