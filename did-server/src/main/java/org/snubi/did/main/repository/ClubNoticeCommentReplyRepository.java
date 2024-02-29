package org.snubi.did.main.repository;

import java.util.List;
import java.util.Optional;
import org.snubi.did.main.entity.ClubNoticeCommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubNoticeCommentReplyRepository extends JpaRepository<ClubNoticeCommentReply, Long> {
	List<ClubNoticeCommentReply> findByClubNoticeComment_ClubNoticeCommentSeqOrderByClubNoticeCommentReplySeqDesc(long clubNoticeCommentSeq);
    Optional<ClubNoticeCommentReply> findByClubNoticeCommentReplySeq(Long clubNoticeCommentReplySeq);
    //Page<ClubNotice> findAllByClub_ClubSeq(Long clubSeq,  Pageable pageable);
	//Page<ClubNotice> findAllByClub_ClubSeqAndClubCategory_ClubCategorySeq(Long clubSeq, Long clubCategorySeq, Pageable pageable);
}
