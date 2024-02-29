package org.snubi.did.main.repository;

import java.util.List;
import java.util.Optional;
import org.snubi.did.main.entity.ClubNoticeLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubNoticeLikeRepository extends JpaRepository<ClubNoticeLike, Long> {
	Optional<ClubNoticeLike> findByClubNotice_ClubNoticeSeqAndCreator(Long clubNoticeSeq, String memberId);
	Integer countByClubNotice_ClubNoticeSeqAndLikeFlag(Long clubNoticeSeq, boolean isLikeFlag);
	List<ClubNoticeLike> findByClubNotice_ClubNoticeSeqAndLikeFlag(Long clubNoticeSeq, boolean isLikeFlag);
}
