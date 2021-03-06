package eu.elraro.jk3rest.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.elraro.jk3rest.model.GameServer;
import eu.elraro.jk3rest.model.MasterServer;
import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;
import eu.elraro.jk3rest.repository.GameServerRepository;

@RestController
public class ServerController {
	
	private static final Log LOG = LogFactory.getLog(ServerController.class);

	@Autowired
	private GameServerRepository gameServerRepository;
	
	@RequestMapping(value = "/servers", method = RequestMethod.GET)
	public ResponseEntity<List<GameServer>> getAllServers() {
		List<GameServer> servers = gameServerRepository.findAll();
		LOG.info("Query all servers");
        return new ResponseEntity<List<GameServer>>(servers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/servers/update", method = RequestMethod.GET)
	public ResponseEntity<Boolean> updateAllServers() {
		gameServerRepository.deleteAll();
		Quake3Protocol quake3Protocol = new Quake3Protocol();
		MasterServer master = new MasterServer("master.jkhub.org", 29060);
		if (master.connect(quake3Protocol) == ServerResponseStatus.OK) {
			for (String serverString : master.getServers()) {
				String[] serverSlipt = serverString.split(":");
				ResponseEntity<GameServer> response = this.getServer(serverSlipt[0], Integer.parseInt(serverSlipt[1]));
				GameServer server = response.getBody();
				if (server.getStatus() == ServerResponseStatus.OK) {
					gameServerRepository.save(server);
				}
			}
			LOG.info("Query update from master server.");
			return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
		}
		LOG.error("Error when query update from master server.");
        return new ResponseEntity<Boolean>(false, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@RequestMapping(value = "/servers/{ip}/{port}/", method = RequestMethod.GET)
	public ResponseEntity<GameServer> getServer(@PathVariable String ip, @PathVariable int port) {
		Quake3Protocol quake3Protocol = new Quake3Protocol();
		GameServer server = new GameServer(ip, port, System.currentTimeMillis());
		ServerResponseStatus status = server.connect(quake3Protocol);
		server.setStatus(status);
		LOG.info("Query server " + ip + ":" + port);
		if (server.getStatus() != ServerResponseStatus.OK) {
			return new ResponseEntity<GameServer>(server, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<GameServer>(server, HttpStatus.OK);
	}

}
