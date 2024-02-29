package org.snubi.did.main.repository;

import java.util.List;
import java.util.Optional;
import org.snubi.did.main.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
	Optional<Club> findByClubSeq(Long clubSeq);
	//Optional<Club> findByClubName(String clubName);
	Page<Club> findAllBy(Pageable pageable);
	Page<Club> findByMemberDid_MemberDidSeqIn(List<Long> MemberDidSeq, Pageable pageable);
	List<Club> findByMemberDid_MemberDidSeqIn(List<Long> MemberDidSeq);
	Page<Club> findByClubSeqIn(List<Long> clubSeq, Pageable pageable);	
	boolean existsByClubNameIgnoreCase(String clubName);
	
	
	
}
