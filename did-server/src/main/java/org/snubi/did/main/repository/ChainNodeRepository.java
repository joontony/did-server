package org.snubi.did.main.repository;

import java.util.Optional;

import org.snubi.did.main.entity.ChainNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChainNodeRepository extends JpaRepository<ChainNode, Long>  {
	Optional<ChainNode> findByChainNodeSeq(Long chainNodeSeq);

}
