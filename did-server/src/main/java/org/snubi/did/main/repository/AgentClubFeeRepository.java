package org.snubi.did.main.repository;

import java.util.Optional;
import org.snubi.did.main.entity.AgentClubFee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentClubFeeRepository extends JpaRepository<AgentClubFee, Long>  {
	Optional<AgentClubFee> findByAgentClub_AgentClubSeqAndMemberDid_MemberDidSeq(Long agentClubseq, Long memberDidSeq);
	Page<AgentClubFee> findByAgentClub_AgentClubSeq(Long agentClubSeq, Pageable pageable);
	Long countByAgentClub_AgentClubSeq(Long agentClubSeq);
}
