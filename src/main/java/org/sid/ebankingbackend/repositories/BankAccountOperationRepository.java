package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.entities.BankAccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountOperationRepository extends JpaRepository<BankAccountOperation,Long> {
  List<BankAccountOperation> findByBankAccountId (String accountId);
  Page<BankAccountOperation> findByBankAccountId (String accountId , PageRequest pageable);

}
