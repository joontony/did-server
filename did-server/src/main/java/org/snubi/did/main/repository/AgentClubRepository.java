package org.snubi.did.main.repository;

import java.util.Optional;

import org.snubi.did.main.entity.AgentClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentClubRepository extends JpaRepository<AgentClub, Long>  {
	Optional<AgentClub> findByAgentClubSeq(Long agentClubSeq);
	Optional<AgentClub> findByClub_ClubSeq(Long clubSeq);
}
