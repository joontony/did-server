package org.snubi.did.main.repository;

import org.snubi.did.main.entity.PersonalMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PersonalMessageRepository extends JpaRepository<PersonalMessage, Long> {
	Page<PersonalMessage> findByClub_ClubSeqAndReceiverMemberId(Long clubSeq, String receiverMemberId,Pageable pageable);
}
