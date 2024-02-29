package org.snubi.did.main.repository;

import java.util.Optional;

import org.snubi.did.main.entity.ClubFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubFeeRepository extends JpaRepository<ClubFee, Long>  {
	Optional<ClubFee> findByClub_ClubSeq(Long clubSeq);
}
