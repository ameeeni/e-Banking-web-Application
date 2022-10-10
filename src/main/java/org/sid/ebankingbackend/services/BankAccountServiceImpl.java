package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.exceptions.InsufficientBalanceException;
import org.sid.ebankingbackend.mappers.BankAccountMapperImp;
import org.sid.ebankingbackend.repositories.BankAccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private BankAccountRepository bankAccountRepository;
    private BankAccountOperationRepository bankAccountOperationRepository;
    private CustomerRepository customerRepository;
    private BankAccountMapperImp dtoMapper;



    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long CustomerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if(customer ==null)
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount  = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setDateCreation(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);


        return dtoMapper.fromCurrentAccount(savedCurrentAccount) ;
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long CustomerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if(customer ==null)
            throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setDateCreation(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingAccount(savedSavingAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
         BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account Not found !"));
         if(bankAccount instanceof SavingAccount){
             SavingAccount savingAccount =  (SavingAccount) bankAccount;
             return dtoMapper.fromSavingAccount(savingAccount);
         } else {
             CurrentAccount currentAccount = (CurrentAccount) bankAccount;
             return dtoMapper.fromCurrentAccount(currentAccount);
         }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, InsufficientBalanceException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account Not found !"));         if(bankAccount.getBalance() < amount){
             throw new InsufficientBalanceException("Balance not sufficient");
         }
        BankAccountOperation bankAccountOperation = new BankAccountOperation();
         bankAccountOperation.setOperationType(OperationType.DEBIT);
         bankAccountOperation.setAmount(amount);
         bankAccountOperation.setDescription(description);
         bankAccountOperation.setDate(new Date());
         bankAccountOperation.setBankAccount(bankAccount);
        bankAccountOperationRepository.save(bankAccountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account Not found !"));    BankAccountOperation bankAccountOperation = new BankAccountOperation();
    bankAccountOperation.setOperationType(OperationType.CREDIT);
    bankAccountOperation.setAmount(amount);
    bankAccountOperation.setDescription(description);
    bankAccountOperation.setDate(new Date());
    bankAccountOperation.setBankAccount(bankAccount);
    bankAccountOperationRepository.save(bankAccountOperation);
    bankAccount.setBalance(bankAccount.getBalance() + amount);
    bankAccountRepository.save(bankAccount);
    }
    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, InsufficientBalanceException {
        debit(accountIdSource , amount , "Transfer to " + accountIdDestination);
        credit(accountIdDestination , amount , "Tranfer from " + accountIdSource);
    }
    @Override
     public List<BankAccountDTO> BankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountsDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
      return bankAccountsDTOS;
    }
     @Override
     public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException{
     Customer customer=   customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
     return dtoMapper.fromCustomer(customer);
     }
     @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }
    @Override
    public List<BankAccountOperationDTO> accountHistory(String accountId){
        List<BankAccountOperation> accountOperations = bankAccountOperationRepository.findByBankAccountId(accountId);
       return accountOperations.stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }
    @Override
    public BankAccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(null);
        if(bankAccount ==null) throw new BankAccountNotFoundException("Bank Account Not Found");
        Page<BankAccountOperation> bankAccountOperationPage = bankAccountOperationRepository.findByBankAccountId(accountId , PageRequest.of(page ,size));
        BankAccountHistoryDTO bankAccountHistoryDTO = new BankAccountHistoryDTO();
        List<BankAccountOperationDTO> bankAccountOperationDTOSList = bankAccountOperationPage.getContent().stream().map(op-> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        bankAccountHistoryDTO.setBankAccountOperationDTOS(bankAccountOperationDTOSList);
        bankAccountHistoryDTO.setAccountId(bankAccount.getId());
        bankAccountHistoryDTO.setBalance(bankAccount.getBalance());
        bankAccountHistoryDTO.setTotalPage(page);
        bankAccountHistoryDTO.setPageSize(size);
        bankAccountHistoryDTO.setTotalPage(bankAccountOperationPage.getTotalPages());

        return bankAccountHistoryDTO;
    }

}

