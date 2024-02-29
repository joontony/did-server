package org.snubi.did.main.repository;

import java.util.Optional;

import org.snubi.did.main.entity.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAccountRepository extends JpaRepository<MemberAccount, Long> {
	boolean existsByMember_MemberIdAndChainAddressPw(String memberId, String chainAddressPw);
    Optional<MemberAccount> findByMember_MemberId(String memberId);
}
