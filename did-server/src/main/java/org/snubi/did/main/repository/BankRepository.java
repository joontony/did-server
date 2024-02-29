package org.snubi.did.main.repository;

import java.util.Optional;
import org.snubi.did.main.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long>  {
	Optional<Bank> findByBankSeq(Long bankSeq); 
}
