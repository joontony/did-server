package org.snubi.did.main.repository;

import java.util.List;
import java.util.Optional;
import org.snubi.did.main.entity.MemberDid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDidRepository extends JpaRepository<MemberDid, Long>{
	Optional<MemberDid> findByMemberDidSeq(Long memberDidSeq);
	//Optional<MemberDid> findByMember_MemberId(String memberId);
	Optional<MemberDid> findByDid(String did);
	List<MemberDid> findByMember_MemberId(String memberId);
}
