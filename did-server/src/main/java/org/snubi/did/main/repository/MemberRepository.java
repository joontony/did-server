package org.snubi.did.main.repository;

import java.util.Optional;
import org.snubi.did.main.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByMemberId(String memberId); 
	Optional<Member> findByMobileNumber(String mobileNumber);
	boolean existsByMemberIdAndMobileAuthNumber(String memberId, String mobileAuthNumber);
	Optional<Member> findByMemberIdAndMobileNumberAndMobileAuthNumber(String memberId, String mobileNumber, String mobileAuthNumber); 
}
