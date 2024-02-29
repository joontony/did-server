package org.snubi.did.main.repository;

import java.util.Optional;

import org.snubi.did.main.entity.PushLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushLogRepository extends JpaRepository<PushLog, Long>{
	Optional<PushLog> findByPushLogSeq(Long pushLogSeq);
}
