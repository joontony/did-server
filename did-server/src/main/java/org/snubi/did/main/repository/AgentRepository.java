package org.snubi.did.main.repository;


import java.util.Optional;

import org.snubi.did.main.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long>  {
	Optional<Agent> findByAgentSeq(Long agentSeq); 
}
