package eu.elraro.jk3rest.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import eu.elraro.jk3rest.model.GameServer;

@CacheConfig(cacheNames="servers")
public interface Jk3RestRepository extends JpaRepository<GameServer, Long> {
	
	@CachePut
	GameServer save(GameServer server);

	@Cacheable
	List<GameServer> findAll();
	
	GameServer findByIpAddressAndPort(String ipAddress, int port);
}
