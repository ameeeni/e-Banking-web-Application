package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter() {
        BankAccount bankAccount = bankAccountRepository.findById("170de839-f197-493a-af06-56b1fa085257").orElse(null);
        System.out.println("*******************************************");
        System.out.println(bankAccount.getId());
        System.out.println(bankAccount.getCustomer().getName());
        System.out.println(bankAccount.getBalance());
        System.out.println(bankAccount.getDateCreation());
        System.out.println(bankAccount.getAccountStatus());
        if (bankAccount instanceof CurrentAccount){
            System.out.println( "Over Draft" +  ((CurrentAccount) bankAccount).getOverDraft());
        }else  if (bankAccount instanceof SavingAccount) {
            System.out.println( "Interest Rate" +   ((SavingAccount) bankAccount).getInterestRate());
        }
        bankAccount.getBankAccountOperationList().forEach(op-> {
            System.out.println(op.getOperationType() + "\t" + op.getAmount() + "\t" + op.getDate());
        });
    }
}
