package org.sid.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.BankAccountDTO;
import org.sid.ebankingbackend.dtos.BankAccountHistoryDTO;
import org.sid.ebankingbackend.dtos.BankAccountOperationDTO;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/accounts/{accountId}")
   public BankAccountDTO getBankAccount( @PathVariable String accountId) throws BankAccountNotFoundException {
    return bankAccountService.getBankAccount(accountId);

}
   @GetMapping("/accounts")
   public List<BankAccountDTO> listAccounts(){
        return  bankAccountService.BankAccountList();
   }
    @GetMapping("/accounts/{accountId}/operations")
   public List<BankAccountOperationDTO> getHistory(@PathVariable String accountId){
      return bankAccountService.accountHistory(accountId);
   }
   @GetMapping("/accounts/{accountsId}/pageOperations")
    public BankAccountHistoryDTO getAccountHistory(@PathVariable String accountId ,
                                                   @RequestParam(name="page" , defaultValue = "0") int page ,
                                                   @RequestParam(name = "size" , defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId , page , size);

    }

}
