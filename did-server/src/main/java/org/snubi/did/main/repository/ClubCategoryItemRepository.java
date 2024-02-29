package org.snubi.did.main.repository;

import java.util.List;
import java.util.Optional;
import org.snubi.did.main.entity.ClubCategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubCategoryItemRepository extends JpaRepository<ClubCategoryItem, Long> {
	Optional<ClubCategoryItem> findByClubCategoryItemSeq(Long clubCategoryItemSeq);
	List<ClubCategoryItem> findByClubCategory_ClubCategorySeq(Long clubCategorySeq);
}
