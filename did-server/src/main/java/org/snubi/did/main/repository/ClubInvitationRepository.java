package org.snubi.did.main.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.snubi.did.main.entity.ClubInvitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface ClubInvitationRepository extends JpaRepository<ClubInvitation, Long>  {
	List<ClubInvitation> findByClub_ClubSeq(Long clubSeq);
	Page<ClubInvitation> findByClub_ClubSeq(Long clubSeq,Pageable pageable);
	Optional<ClubInvitation> findByMobileNumber(String mobileNumber);
	Optional<ClubInvitation> findByMobileNumberAndClub_ClubSeq(String mobileNumber, Long clubSeq);
	Optional<ClubInvitation> findByClubInvitationSeq(Long clubInvitationSeq);
	List<ClubInvitation> findByClub_ClubSeqAndSendFlag(Long clubSeq, boolean sendFlag);
	List<ClubInvitation> findByClub_ClubSeqAndMobileNumber(Long clubSeq, String mobileNumber);
	List<ClubInvitation> findByClub_ClubSeqAndValid(Long clubSeq, boolean valid);
	Page<ClubInvitation> findByClub_ClubSeqAndMobileNumberNotIn(Long clubSeq,List<String> mobileNumber,Pageable pageable);
	
	@Modifying
    @Query("UPDATE ClubInvitation c SET c.sendFlag = :sendFlag, c.created = :currentDate, c.updated = :currentDate WHERE c.clubInvitationSeq IN :clubInvitationSes")
    void updateSendFlagByIds(@Param("sendFlag") boolean sendFlag, @Param("currentDate") LocalDateTime currentDate,@Param("clubInvitationSes") List<Long> clubInvitationSeqs);
}
