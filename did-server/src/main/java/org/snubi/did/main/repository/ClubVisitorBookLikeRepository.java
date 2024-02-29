package org.snubi.did.main.repository;

import java.util.List;
import java.util.Optional;

import org.snubi.did.main.entity.ClubVisitorBookLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubVisitorBookLikeRepository extends JpaRepository<ClubVisitorBookLike, Long> {
   Optional<ClubVisitorBookLike> findByClubVisitorBook_ClubVisitorBookSeqAndCreator(Long clubVisitorBookSeq, String memberId);
   Integer countByClubVisitorBook_ClubVisitorBookSeqAndLikeFlag(Long clubVisitorBookSeq, boolean isLikeFlag);
   List<ClubVisitorBookLike> findByClubVisitorBook_ClubVisitorBookSeqAndLikeFlag(Long clubVisitorBookSeq, boolean isLikeFlag);
}
