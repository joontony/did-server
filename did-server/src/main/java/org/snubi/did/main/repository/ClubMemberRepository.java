package org.snubi.did.main.repository;

import java.util.List;
import java.util.Optional;
import org.snubi.did.main.entity.ClubMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
	Optional<ClubMember> findByClubMemberSeq(Long clubMemberSeq);
	Optional<ClubMember> findByClub_ClubSeqAndMemberDid_MemberDidSeq(Long clubSeq, Long memberDidSeq);
	//Optional<ClubMember> findByClub_ClubSeqAndClubCategory_ClubCategorySeq(Long clubSeq, Long clubCategorySeq);
	List<ClubMember> findByClub_ClubSeq(Long clubSeq);
	Page<ClubMember> findByClub_ClubSeq(Long clubSeq,Pageable pageable);
	List<ClubMember> findByMemberDid_MemberDidSeq(Long memberDidSeq);
	List<ClubMember> findByMemberDid_MemberDidSeqAndClubRole_ClubRoleSeq(Long memberDidSeq, Long clubRoldSeq);
	Optional<ClubMember> findByClub_ClubSeqAndMemberDid_MemberDidSeqIn(Long clubSeq, List<Long> memberDidSeq);
	Optional<ClubMember> findByClubInvitation_ClubInvitationSeq(Long clubInvitationSeq);
}
