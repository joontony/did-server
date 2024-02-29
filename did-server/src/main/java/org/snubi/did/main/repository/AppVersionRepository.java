package org.snubi.did.main.repository;

import java.util.Optional;
import org.snubi.did.main.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long>  {
	Optional<AppVersion> findByOs(String os); 
}