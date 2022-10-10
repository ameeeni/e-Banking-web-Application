package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.exceptions.InsufficientBalanceException;

import java.util.List;

public interface BankAccountService {




     CustomerDTO saveCustomer(CustomerDTO customerDTO);

     CurrentAccountDTO saveCurrentBankAccount(double initialBalance , double overDraft, Long CustomerId) throws CustomerNotFoundException;
     SavingAccountDTO saveSavingBankAccount(double initialBalance , double interestRate, Long CustomerId) throws CustomerNotFoundException;
     List<CustomerDTO> listCustomers();
     BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
     void debit (String accoundId , double amount , String description) throws BankAccountNotFoundException, InsufficientBalanceException;
     void credit (String accoundId , double amount , String description) throws BankAccountNotFoundException;
     void transfer(String accountIdSource , String accountIdDestination , double amount) throws BankAccountNotFoundException, InsufficientBalanceException;
     public List<BankAccountDTO> BankAccountList();

     CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

     CustomerDTO updateCustomer(CustomerDTO customerDTO);

     void deleteCustomer(Long customerId);

     List<BankAccountOperationDTO> accountHistory(String accountId);


      BankAccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
