package org.snubi.did.main.repository;

import java.util.Optional;

import org.snubi.did.main.entity.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsLogRepository extends JpaRepository<SmsLog, Long>{
	Optional<SmsLog> findBySmsLogSeq(Long smsLogSeq);
}
