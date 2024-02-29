package org.snubi.did.main.repository;

import java.util.Optional;
import org.snubi.did.main.entity.ClubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubCategoryRepository extends JpaRepository<ClubCategory, Long> {
	Optional<ClubCategory> findByClubCategorySeq(Long clubCategorySeq);
}
