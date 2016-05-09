package eu.elraro.jk3rest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.elraro.jk3rest.model.GameServer;
import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;

public class Quake3Test {

    private Quake3Protocol quake3Protocol;
    private GameServer server;

    @Before
    public void setUp()
    {
        quake3Protocol = new Quake3Protocol();
    }

    @After
    public void tearDown()
    {
        server.disconnect();
    }

    @Test
    //@Ignore("avoid querying the server too frequently")
    public void queryRealServer()
    {
        server = new GameServer("192.223.29.244", 29070, quake3Protocol);
        server.connect();
        if(quake3Protocol.query("getstatus") == ServerResponseStatus.OK) {
            quake3Protocol.updateServerInfo(server);
        }
    }

    @Test
    public void queryWrongAddress() {
        server = new GameServer("myacxy.net", 28071, quake3Protocol);
        // valid address + wrong port
        assertEquals(server.connect(), ServerResponseStatus.CONNECTED);
        assertEquals(server.update(), ServerResponseStatus.SOCKET_TIMEOUT_EXCEPTION);

        // valid address + port out of range
        server = new GameServer("myacxy.net", 99999, quake3Protocol);
        assertEquals(server.connect(), ServerResponseStatus.ILLEGAL_ARGUMENT_EXCEPTION);
        assertEquals(server.update(), ServerResponseStatus.ILLEGAL_ARGUMENT_EXCEPTION);

        // invalid address + valid port
        server = new GameServer("myacxydasd.next@", 28070, quake3Protocol);
        assertEquals(server.connect(), ServerResponseStatus.UNKNOWN_HOST_EXCEPTION);
        
        server = new GameServer("myacxydasd.next", 28070, quake3Protocol);
        server.connect();
        assertEquals(quake3Protocol.query("getstatus"), ServerResponseStatus.UNKNOWN_HOST_EXCEPTION);
    }
}