package eu.elraro.jk3rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.elraro.jk3rest.model.GameServer;
import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;

public class ServerTest {

	private Quake3Protocol quake3Protocol;
	private GameServer server;

	@Before
	public void setUp() {
		quake3Protocol = new Quake3Protocol();
	}

	@After
	public void tearDown() {
		quake3Protocol.disconnect();
	}

	@Test
	public void queryRealServer() {
		server = new GameServer("89.36.214.51", 29070, System.currentTimeMillis());
		server.connect(quake3Protocol);
		if (quake3Protocol.query("getstatus") == ServerResponseStatus.OK) {
			quake3Protocol.updateServerInfo(server);
			if (!server.getHostName().contains("JK|NG")) {
				assertTrue(false);
			}
		} else {
			assertTrue(false);
		}
	}

	@Test
	public void queryWrongAddress() {
		server = new GameServer("myacxy.net", 29071, System.currentTimeMillis());
		// valid address + wrong port
		assertEquals(server.connect(quake3Protocol), ServerResponseStatus.SOCKET_TIMEOUT_EXCEPTION);

		// valid address + port out of range
		server = new GameServer("myacxy.net", 99999, System.currentTimeMillis());
		assertEquals(server.connect(quake3Protocol), ServerResponseStatus.ILLEGAL_ARGUMENT_EXCEPTION);

		// invalid address + valid port
		server = new GameServer("myacxydasd.next@", 28070, System.currentTimeMillis());
		assertEquals(server.connect(quake3Protocol), ServerResponseStatus.UNKNOWN_HOST_EXCEPTION);
	}
}