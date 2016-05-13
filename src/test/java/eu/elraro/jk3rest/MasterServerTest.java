package eu.elraro.jk3rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.elraro.jk3rest.model.MasterServer;
import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;

public class MasterServerTest {

	private Quake3Protocol quake3Protocol;

	@Before
	public void setUp() {
		quake3Protocol = new Quake3Protocol();
	}

	@After
	public void tearDown() {
		quake3Protocol.disconnect();
	}

	@Test
	public void queryRealMasterServer() {
		MasterServer master = new MasterServer("master.jkhub.org", 29060);
		if (master.connect(quake3Protocol) == ServerResponseStatus.OK) {
			System.out.println(master.getServers());
		}
	}
}