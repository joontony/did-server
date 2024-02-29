package org.snubi.did.main.repository;

import java.util.Optional;
import org.snubi.did.main.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository  extends JpaRepository<ChatRoom, Long> {
	Optional<ChatRoom> findByClub_ClubSeqAndSessionId(Long clubSeq, String sessionId);
}
