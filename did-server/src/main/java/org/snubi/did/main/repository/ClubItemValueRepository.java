package org.snubi.did.main.repository;

import org.snubi.did.main.entity.ClubItemValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubItemValueRepository extends JpaRepository<ClubItemValue, Long>  {

}
