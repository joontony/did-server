package org.snubi.did.main.repository;

import java.util.Optional;

import org.snubi.did.main.entity.PushType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushTypeRepository extends JpaRepository<PushType, Long>{
	Optional<PushType> findByPushTypeSeq(Long pushTypeSeq);
}
