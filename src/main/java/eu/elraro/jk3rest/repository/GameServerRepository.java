package eu.elraro.jk3rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.elraro.jk3rest.model.GameServer;

public interface GameServerRepository extends JpaRepository<GameServer, Long> {
	
	GameServer save(GameServer server);

	List<GameServer> findAll();
	
	GameServer findByIpAddressAndPort(String ipAddress, int port);
}
