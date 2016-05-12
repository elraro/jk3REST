package eu.elraro.jk3rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.elraro.jk3rest.model.GameServer;
import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;
import eu.elraro.jk3rest.repository.Jk3RestRepository;

@RestController
public class Jk3RestController {

	@Autowired
	private Jk3RestRepository jk3RestRepository;
	
	@RequestMapping(value = "/servers", method = RequestMethod.GET)
	public ResponseEntity<List<GameServer>> getAllServers() {
		List<GameServer> servers = jk3RestRepository.findAll();
        return new ResponseEntity<List<GameServer>>(servers, HttpStatus.OK);
	}

	@RequestMapping(value = "/servers/{ip}/{port}/", method = RequestMethod.GET)
	public ResponseEntity<GameServer> getServer(@PathVariable String ip, @PathVariable int port) {
		
		GameServer serverDatabase = jk3RestRepository.findByIpAddressAndPort(ip, port);
		if (serverDatabase != null) {
			if (serverDatabase.getTimestamp() + 1000 > System.currentTimeMillis()) {
				System.out.println("asdasd");
				return new ResponseEntity<GameServer>(serverDatabase, HttpStatus.OK);
			} else {
				jk3RestRepository.delete(serverDatabase);
			}
		}
		
		Quake3Protocol quake3Protocol = new Quake3Protocol();
		GameServer server = new GameServer(ip, port, quake3Protocol, System.currentTimeMillis());
		ServerResponseStatus status = server.connect();
		server.update();
		
		jk3RestRepository.save(server);

//        switch(status) {
//        	case OK:
//        	case CONNECTED:
//                return new ResponseEntity<GameServer>(server, HttpStatus.OK);
//        	case ILLEGAL_ARGUMENT_EXCEPTION:
//        	case UNKNOWN_HOST_EXCEPTION:
//        	case IO_EXCEPTION:
//        		return new ResponseEntity<GameServer>(server, HttpStatus.BAD_REQUEST);
//        	case SOCKET_EXCEPTION:
//        	case SOCKET_TIMEOUT_EXCEPTION:
//        	default:
//        		return new ResponseEntity<GameServer>(server, HttpStatus.REQUEST_TIMEOUT);
//        }

		return new ResponseEntity<GameServer>(server, HttpStatus.OK);
		
	}

}
