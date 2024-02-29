package org.snubi.did.main.repository;

import java.util.Optional;
import org.snubi.did.main.entity.ClubNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubNoticeRepository extends JpaRepository<ClubNotice, Long> {
    Optional<ClubNotice> findByClubNoticeSeq(Long clubNoticeSeq);
    Page<ClubNotice> findAllByClub_ClubSeq(Long clubSeq,  Pageable pageable);
    Page<ClubNotice> findAllByClub_ClubSeqAndValid(Long clubSeq, boolean valid, Pageable pageable);
    Optional<ClubNotice> findByClubNoticeSeqAndCreator(Long clubNoticeSeq, String memberId);
	//Page<ClubNotice> findAllByClub_ClubSeqAndClubCategory_ClubCategorySeq(Long clubSeq, Long clubCategorySeq, Pageable pageable);
}
