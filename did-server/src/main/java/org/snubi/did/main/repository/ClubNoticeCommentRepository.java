package org.snubi.did.main.repository;

import java.util.Optional;
import org.snubi.did.main.entity.ClubNoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ClubNoticeCommentRepository extends JpaRepository<ClubNoticeComment, Long> {
   Optional<ClubNoticeComment> findByClubNoticeCommentSeq(Long clubNoticeCommentSeq);
   Optional<ClubNoticeComment> findByClubNoticeCommentSeqAndCreator(Long clubNoticeCommentSeq, String memberId);
   Page<ClubNoticeComment> findAllByClubNotice_ClubNoticeSeqAndValid(Long clubNoticeSeq, boolean valid,  Pageable pageable);
	//Page<ClubNotice> findAllByClub_ClubSeqAndClubCategory_ClubCategorySeq(Long clubSeq, Long clubCategorySeq, Pageable pageable);
}
