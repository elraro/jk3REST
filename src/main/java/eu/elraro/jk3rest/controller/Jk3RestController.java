package eu.elraro.jk3rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.elraro.jk3rest.model.GameServer;
import eu.elraro.jk3rest.query.Quake3Protocol;
import eu.elraro.jk3rest.query.ServerResponseStatus;

@Controller
public class Jk3RestController {

	//@Autowired
	//private Jk3RestRepository jk3RestRepository;

	@RequestMapping(value = "/servidores", method = RequestMethod.GET)
	public ResponseEntity<String> listAllServers(Model model) {
		Quake3Protocol quake3Protocol = new Quake3Protocol();
		GameServer server = new GameServer("192.223.29.244", 29070, quake3Protocol);
        server.connect();
        if(quake3Protocol.query("getstatus") == ServerResponseStatus.OK) {
            quake3Protocol.updateServerInfo(server);
        }
        return new ResponseEntity<String>(server.toString(), HttpStatus.OK);
	}

}
