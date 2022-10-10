package org.sid.ebankingbackend;

import org.sid.ebankingbackend.dtos.BankAccountDTO;
import org.sid.ebankingbackend.dtos.CurrentAccountDTO;
import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.dtos.SavingAccountDTO;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.AccountStatus;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.exceptions.InsufficientBalanceException;
import org.sid.ebankingbackend.repositories.BankAccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.services.BankAccountService;
import org.sid.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankingBackendApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner (BankAccountService bankAccountService){
     return args -> {
         Stream.of("Eya" , "Mayssa" , "Firas").forEach(name ->{
             CustomerDTO customerDTO = new CustomerDTO();
             customerDTO.setName(name);
             customerDTO.setEmail(name + "@gmail.com");
             bankAccountService.saveCustomer(customerDTO);
         });
         bankAccountService.listCustomers().forEach(customer -> {
             try {
                 bankAccountService.saveCurrentBankAccount(Math.random()*90000 , 9000 , customer.getId());
                 bankAccountService.saveSavingBankAccount(Math.random()*120000 , 5.5 , customer.getId());


             } catch (CustomerNotFoundException e) {
                 e.printStackTrace();
             }
         });
         List<BankAccountDTO> bankAccounts = bankAccountService.BankAccountList();
         for(BankAccountDTO bankAccount:bankAccounts ){
             for(int i =0 ; i<10 ; i++){
                 String accountId;
                 if(bankAccount instanceof SavingAccountDTO){
                     accountId = ((SavingAccountDTO) bankAccount).getId();
                 }else{
                     accountId =((CurrentAccountDTO) bankAccount).getId();
                 }
                 bankAccountService.credit(accountId, 10000 + Math.random()*120000 , "Credit");
                 bankAccountService.debit(accountId, 1000 + Math.random()*9000 , "Debit");

             }
         }
     };

    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository ,
                            BankAccountRepository bankAccountRepository ,
                            BankAccountOperationRepository bankAccountOperationRepository) {
        return args -> {
            Stream.of("Hassan", "Yassin", "Aicha").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust ->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setDateCreation(new Date());
                currentAccount.setAccountStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                /**Je suis entrain de crée un compte courant pour chaque customer*/
                bankAccountRepository.save(currentAccount);


                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setDateCreation(new Date());
                savingAccount.setAccountStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);


                /**Je suis entrain de crée un compte d'é pargne  pour chaque customer*/
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(acc->{ /**pour chaque bank account je vais faire 5 operation**/
                for (int i =0  ; i <5 ; i++) {
                    BankAccountOperation bankAccountOperation = new BankAccountOperation();
                    bankAccountOperation.setDate(new Date());
                    bankAccountOperation.setAmount(Math.random()*12000);
                    bankAccountOperation.setOperationType( Math.random() >0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    bankAccountOperation.setBankAccount(acc);
                    bankAccountOperationRepository.save(bankAccountOperation);
                }
            });



        };
    }
}
