package eu.elraro.jk3rest.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import eu.elraro.jk3rest.model.MasterServer;

@CacheConfig(cacheNames="masterServers")
public interface MasterServerRepository extends JpaRepository<MasterServer, Long> {
	
//	@CacheEvict(allEntries=true)
//	GameServer save(GameServer server);

	@Cacheable
	List<MasterServer> findAll();
	
//	GameServer findByIpAddressAndPort(String ipAddress, int port);
}
