package org.snubi.did.main.repository;

import java.util.Optional;
import org.snubi.did.main.entity.ClubVisitorBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubVisitorBookRepository extends JpaRepository<ClubVisitorBook, Long> {
    Optional<ClubVisitorBook> findByClubVisitorBookSeq(Long clubVisitorBookSeq);
    Optional<ClubVisitorBook> findByClubVisitorBookSeqAndCreator(Long clubVisitorBookSeq, String memberId);
    Page<ClubVisitorBook> findAllByClub_ClubSeq(Long clubSeq,  Pageable pageable);
	//Page<ClubNotice> findAllByClub_ClubSeqAndClubCategory_ClubCategorySeq(Long clubSeq, Long clubCategorySeq, Pageable pageable);
}
