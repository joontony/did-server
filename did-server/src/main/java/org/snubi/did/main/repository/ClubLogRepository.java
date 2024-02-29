package org.snubi.did.main.repository;

import org.snubi.did.main.entity.ClubLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubLogRepository extends JpaRepository<ClubLog, Long>{

}
