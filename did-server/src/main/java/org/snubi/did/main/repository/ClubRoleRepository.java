package org.snubi.did.main.repository;

import java.util.Optional;

import org.snubi.did.main.entity.ClubRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRoleRepository extends JpaRepository<ClubRole, Long> {
	Optional<ClubRole> findByClubRoleSeq(Long clubRoleSeq);
}
