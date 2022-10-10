package org.sid.ebankingbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.entities.BankAccountOperation;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
public  class CurrentAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date dateCreation;
    private AccountStatus accountStatus;
    private CustomerDTO customerDTO;
    private double overDraft;
}
