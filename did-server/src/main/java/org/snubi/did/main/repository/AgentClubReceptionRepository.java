//package org.snubi.did.main.repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import org.snubi.did.main.entity.AgentClubReception;
//import org.snubi.did.main.entity.AgentClubWaiting;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface AgentClubReceptionRepository extends JpaRepository<AgentClubReception, Long>  {
////	Optional<AgentClubWaiting> findByAgentClub_AgentClubSeqAndMemberDid_MemberDidSeq(Long agentClubseq, Long memberDidSeq);
////	Optional<AgentClubWaiting> findByAgentClubWaitingSeq(Long agentClubWaitingSeq);
////	Page<AgentClubReception> findByAgentClub_AgentClubSeq(Long agentClubSeq, Pageable pageable);
////	Long countByAgentClub_AgentClubSeqAndCreatedBetween(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd);
//	Page<AgentClubReception> findByAgentClub_AgentClubSeqAndCreatedBetween(Long agentClubSeq, Pageable pageable, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd);
////	Integer countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlag(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean flag);
////	List<AgentClubWaiting> findByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagOrderByAgentClubWaitingSeqAsc(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean flag);
////	List<AgentClubWaiting> findByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagOrderByAgentClubWaitingSeqDesc(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd, boolean flag);
////	List<AgentClubWaiting> findByAgentClub_AgentClubSeqAndCreatedBetweenOrderByAgentClubWaitingSeqAsc(Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd);
//}
