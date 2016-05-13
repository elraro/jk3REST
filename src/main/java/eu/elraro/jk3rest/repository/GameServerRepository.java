package eu.elraro.jk3rest.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import eu.elraro.jk3rest.model.GameServer;

@CacheConfig(cacheNames="gameServers")
public interface GameServerRepository extends JpaRepository<GameServer, Long> {
	
	@CacheEvict(allEntries=true)
	GameServer save(GameServer server);

	@Cacheable
	List<GameServer> findAll();
	
	GameServer findByIpAddressAndPort(String ipAddress, int port);
}
