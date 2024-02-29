package org.snubi.did.main.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.snubi.did.main.entity.AgentClubWaiting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentClubWaitingRepository extends JpaRepository<AgentClubWaiting, Long>  {
	//Optional<AgentClubWaiting> findByAgentClub_AgentClubSeqAndMemberDid_MemberDidSeq(Long agentClubseq, Long memberDidSeq);
	Optional<AgentClubWaiting> findByAgentClubWaitingSeq(Long agentClubWaitingSeq);
	//Page<AgentClubWaiting> findByAgentClub_AgentClubSeq(Long agentClubSeq, Pageable pageable);
	
	Long countByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlag(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean WaitingFlag);	
	Page<AgentClubWaiting> findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlag(Long agentClubSeq, Pageable pageable, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean WaitingFlag);
	Integer countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlag(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean flag,  boolean WaitingFlag);
	List<AgentClubWaiting> findByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlagOrderByAgentClubWaitingSeqAsc(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean flag, boolean WaitingFlag);
	List<AgentClubWaiting> findByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlagOrderByAgentClubWaitingSeqDesc(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean flag, boolean WaitingFlag);
	List<AgentClubWaiting> findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlagOrderByAgentClubWaitingSeqAsc(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean WaitingFlag);



	
	Page<AgentClubWaiting> findByAgentClub_AgentClubSeqAndCreatedBetween(Long agentClubSeq, Pageable pageable, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd);
	Integer countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlag(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean flag);
	Long countByAgentClub_AgentClubSeqAndCreatedBetween(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd);	
	

}
